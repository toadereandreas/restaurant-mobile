package com.gradysbooch.restaurant.repository.networkRepository

import android.content.Context
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gradysbooch.restaurant.*
import com.gradysbooch.restaurant.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import okhttp3.*
import okio.ByteString.Companion.decodeBase64
import java.lang.reflect.Type
import kotlin.math.roundToInt

private const val WEBSOCKET_URL = "ws://restaurant.playgroundev.com:5000/ws"
private const val GRAPHQL_URL = "http://restaurant.playgroundev.com/graphql/"
private const val EMAIL = "admin@welcome.com"
private const val PASSWORD = "welcome"
//"ws://restaurant.playgroundev.com:5000/ws"
//"http://restaurant.playgroundev.com/graphql/"
//"http://halex193.go.ro:8000/graphql/"

class NetworkRepository(context: Context) : NetworkRepositoryInterface {
    private val gson = Gson().newBuilder().create()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val internalOnlineStatus =
        MutableStateFlow(false) //Improvement: implement proper online status tracking
    override val onlineStatus: Flow<Boolean> = internalOnlineStatus

    private val authorizationInterceptor = AuthorizationInterceptor("")
    private val okHttpClient =
        OkHttpClient.Builder().addInterceptor(authorizationInterceptor).build()
    private val apolloClient = ApolloClient.builder().serverUrl(GRAPHQL_URL).build()

    private var userIdCache: Int? = null

    private suspend fun getUserId(): Int {
        userIdCache ?: run {
            login()
        }
        return userIdCache ?: error("Null user id")
    }

    override suspend fun getMenuItems(): Set<MenuItem>  = withContext(Dispatchers.IO){
        val list = runQuerySafely<GetMenuItemsQuery.Data>(GetMenuItemsQuery()).menuItems?.data
            ?: error("ApolloFailure: menu items returned null.")

        return@withContext list.filterNotNull()
            .map { MenuItem(it.id.toString(), it.internalName, it.price.roundToInt()) }.toSet()
    }

    override fun getTables(): Flow<List<Table>> =
        subscribe("serving", object : TypeToken<ArrayList<Table>>() {}.type)

    override fun clientOrders(): Flow<List<Order>> =
        subscribe<List<Order>>("order", object : TypeToken<ArrayList<Order>>() {}.type)
            .shareIn(CoroutineScope(SupervisorJob()), SharingStarted.Lazily, replay = 1)

    override fun orderItems(): Flow<List<OrderItem>> =
        subscribe("ordermenuitem", object : TypeToken<ArrayList<OrderItem>>() {}.type)

    override suspend fun updateOrder(orderWithMenuItems: OrderWithMenuItems) {
        val matchingOrder = _queryOrderByForeignKeys(
            orderWithMenuItems.order.tableUID,
            orderWithMenuItems.order.orderColor
        )

        val id = matchingOrder.gid as String
        val locked = matchingOrder.locked

        apolloClient.mutate(
            UpdateOrderMutation(
                id,
                Input.fromNullable(orderWithMenuItems.order.tableUID),
                Input.fromNullable(orderWithMenuItems.order.orderColor),
                Input.fromNullable(locked),
                Input.fromNullable(orderWithMenuItems.order.note)
            )
        ).await()
    }

    override suspend fun clearCall(tableUID: String) :Unit = withContext(Dispatchers.IO) {
        val tableUidProper = tableUID//_queryTableGidByTableId(tableID)

        apolloClient.mutate(ClearCallMutation(tableUidProper)).await()
    }

    override suspend fun unlockOrder(tableUID: String, color: String) : Unit  = withContext(Dispatchers.IO){
        val id = _queryOrderByForeignKeys(tableUID, color).gid as String

        apolloClient.mutate(UnlockOrderMutation(id)).await()
    }

    override suspend fun lockOrder(tableUID: String, color: String) : Unit = withContext(Dispatchers.IO){
        val id = _queryOrderByForeignKeys(tableUID, color).gid as String

        Log.d("UndoTag", id)

        apolloClient.mutate(LockOrderMutation(id)).await()
    }

    override suspend fun clearTable(tableUID: String) = withContext(Dispatchers.IO){
        val orders = runQuerySafely<GetOrdersQuery.Data>(GetOrdersQuery()).orders?.data
            ?: error("ApolloFailure: orders returned null.")

        orders.forEach {
            if (it != null && it.serving.gid == tableUID) {
                apolloClient.mutate(DeleteOrderMutation(it.gid as String)).await()
            }
        }

        //todo request code regeneration
    }

    override suspend fun createOrderItem(orderItem: OrderItem) : Unit  = withContext(Dispatchers.IO){
        val id = _queryOrderByForeignKeys(orderItem.tableUID, orderItem.orderColor).gid as String

        apolloClient.mutate(CreateOrderMenuItemMutation(
            Input.fromNullable(id),
            Input.fromNullable(orderItem.menuItemUID),
            Input.fromNullable(orderItem.quantity)
        )).await()
    }

    override suspend fun updateOrderItem(orderItem: OrderItem) : Unit = withContext(Dispatchers.IO){
        val idOrderMenuItem= _queryOrderMenuItemByForeignKeys(orderItem.menuItemUID, orderItem.orderColor, orderItem.tableUID).gid as String

        apolloClient.mutate(UpdateOrderMenuItemMutation(
            idOrderMenuItem,
            Input.fromNullable(orderItem.menuItemUID),
            Input.fromNullable(orderItem.quantity.toString())
        )).await()
    }

    override suspend fun createOrder(color: String, tableUID: String) {
        apolloClient.mutate(CreateOrderMutation(
            Input.fromNullable(color),
            Input.fromNullable(tableUID)
        )).await()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T> subscribe(endpoint: String, type: Type): Flow<T> = callbackFlow<T> {
        val listener = webSocketListener(channel, type)

        val userId = getUserId()

        val websocket = okHttpClient.newWebSocket(
            Request.Builder().url("$WEBSOCKET_URL/$endpoint/$userId/").build(),
            listener
        )

        awaitClose { websocket.close(1001, "Listener closed") }
    }.flowOn(Dispatchers.IO)

    private fun <T> CoroutineScope.webSocketListener(channel: SendChannel<T>, type: Type) =
        object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {

                Log.d("UndoTag", "Socket got raw $text")

                val receivedValue: T = gson.fromJson(text, type)

                try {
                    channel.sendBlocking(receivedValue)
                } catch (e: Exception) {
                    //Ignored
                }
            }

            override fun onFailure(webSocket: WebSocket, cause: Throwable, response: Response?) {
                cancel(CancellationException("Websocket error", cause))
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                channel.close()
            }
        }

    private suspend fun login() = withContext(Dispatchers.IO){
        val login = apolloClient.mutate(LoginMutation(Input.fromNullable(EMAIL), PASSWORD))
            .await().data?.tokenAuth

        authorizationInterceptor.token = login?.token
            ?: error("ApolloFailure: Received login token null")

        val userId = login.user?.id
            ?: error("ApolloFailure: Received login user id null")

        userIdCache = Regex("[0-9]+").find(userId.decodeBase64().toString())?.value?.toInt()
    }

    private suspend inline fun <reified T : Operation.Data> runQuerySafely(GQLQuery: Query<*, *, *>): T  = withContext(Dispatchers.IO){
        try {
            val result = (apolloClient.query(GQLQuery).await().data as? T
                ?: error("ApolloFailure: Returned null."))

            internalOnlineStatus.emit(true)
            return@withContext result

        } catch (e: ApolloException) {
            internalOnlineStatus.emit(false)
            throw e
        }
    }

    private suspend fun _queryOrderByForeignKeys (
        tableUID: String,
        color: String
    ): GetOrdersQuery.Data1  = withContext(Dispatchers.IO) {
        val orders = runQuerySafely<GetOrdersQuery.Data>(GetOrdersQuery()).orders?.data
            ?: error("ApolloFailure: orders returned null.")

        val matchingOrder = orders.filterNotNull()
            .find { it.color == color && it.id == tableUID }
            ?: error("ApolloFailure: failed to get order")
        return@withContext matchingOrder
    }

    private suspend fun _queryOrderMenuItemByForeignKeys (
        menuItemId: String,
        orderColor: String,
        tableUID: String,
    ): GetOrderMenuItemsQuery.Data1  = withContext(Dispatchers.IO) {
        val orders = runQuerySafely<GetOrderMenuItemsQuery.Data>(GetOrdersQuery()).orderMenuItems?.data
            ?: error("ApolloFailure: ordermenuitems returned null.")

        val matchingOrder = orders.filterNotNull()
            .find { it.menuItemId == menuItemId && it.color ==  orderColor && it.servingId == tableUID}
            ?: error("ApolloFailure: failed to get order")

        return@withContext matchingOrder
    }
}
