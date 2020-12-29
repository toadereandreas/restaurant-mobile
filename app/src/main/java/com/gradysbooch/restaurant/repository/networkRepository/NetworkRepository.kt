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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import okhttp3.*
import okio.ByteString.Companion.decodeBase64
import kotlin.math.roundToInt

private const val WEBSOCKET_URL = "ws://localhost:8000/ws"
private const val GRAPHQL_URL = "http://localhost:8000/graphql/"
private const val EMAIL = "admin@welcome.com"
private const val PASSWORD = "welcome"
//"http://restaurant.playgroundev.com/graphql/"
//"http://halex193.go.ro:8000/graphql/"

class NetworkRepository(context: Context) : NetworkRepositoryInterface {
    private val gson = Gson().newBuilder().excludeFieldsWithoutExposeAnnotation().create()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val internalOnlineStatus =
        MutableStateFlow(false) //todo implement proper online status tracking
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

    private suspend fun login() {
        val login = apolloClient.mutate(LoginMutation(Input.fromNullable(EMAIL), PASSWORD))
            .await().data?.tokenAuth

        authorizationInterceptor.token = login?.token
            ?: error("ApolloFailure: Received login token null")

        val userId = login.user?.id
            ?: error("ApolloFailure: Received login user id null")

        userIdCache = Regex("[0-9]+").find(userId.decodeBase64().toString())?.value?.toInt()
    }

    override suspend fun getMenuItems(): Set<MenuItem> {
        val list = runQuerySafely<GetMenuItemsQuery.Data>(GetMenuItemsQuery()).menuItems?.data
            ?: error("ApolloFailure: menu items returned null.")

        return list.filterNotNull()
            .map { MenuItem(it.id.toString(), it.internalName, it.price.roundToInt()) }.toSet()
    }

    override fun getTables(): Flow<Set<Table>> = (subscribe<ArrayList<Table>>("serving")).map { it.toSet() }

    override fun clientOrders(): Flow<List<Order>> = subscribe("order")

    override fun orderItems(): Flow<List<OrderItem>> = subscribe("ordermenuitem")

    override suspend fun clearCall(tableUID: String) {
        apolloClient.mutate(ClearCallMutation(tableUID))
    }

    override suspend fun updateOrder(orderWithMenuItems: OrderWithMenuItems) {
        val orders = runQuerySafely<GetOrdersQuery.Data>(GetOrdersQuery()).orders?.data
            ?: error("ApolloFailure: orders returned null.")

        val id = orders.filterNotNull()
            .find{it->it.color == orderWithMenuItems.order.orderColor && it.servingId == orderWithMenuItems.order.tableUID}
            ?.id ?: error("ApolloFailure: failed to get order id")

        val locked = orders.filterNotNull()
            .find{it->it.color == orderWithMenuItems.order.orderColor && it.servingId == orderWithMenuItems.order.tableUID}
            ?.locked ?: error("ApolloFailure: failed to get order locked")

        apolloClient.mutate(UpdateOrderMutation(id,
            Input.fromNullable(orderWithMenuItems.order.tableUID),
            Input.fromNullable(orderWithMenuItems.order.orderColor),
            Input.fromNullable(locked),
            Input.fromNullable(orderWithMenuItems.order.note)))
    }

    override suspend fun unlockOrder(tableUID: String, color: String) {
        val orders = runQuerySafely<GetOrdersQuery.Data>(GetOrdersQuery()).orders?.data
            ?: error("ApolloFailure: orders returned null.")

        val id = orders.filterNotNull()
            .find{it->it.color == color && it.servingId == tableUID}
            ?.id ?: error("ApolloFailure: failed to get order id")

        apolloClient.mutate(UnlockOrderMutation(id))
    }

    override suspend fun lockOrder(tableUID: String, color: String) {
        val orders = runQuerySafely<GetOrdersQuery.Data>(GetOrdersQuery()).orders?.data
            ?: error("ApolloFailure: orders returned null.")

        val id = orders.filterNotNull()
            .find{it->it.color == color && it.servingId == tableUID}
            ?.id ?: error("ApolloFailure: failed to get order id")

        apolloClient.mutate(LockOrderMutation(id))
    }

    private suspend inline fun <reified T : Operation.Data> runQuerySafely(GQLQuery: Query<*, *, *>): T {
        try {
            val result = (apolloClient.query(GQLQuery).await().data as? T
                ?: error("ApolloFailure: Returned null."))

            internalOnlineStatus.emit(true)
            return result

        } catch (e: ApolloException) {
            internalOnlineStatus.emit(false)
            throw e
        }
    } //todo function that gets order details from color and tableUID, possibly a dto as well, because we don't have locked in our objects

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun <T> subscribe(endpoint: String): Flow<T> = callbackFlow {
        val listener = webSocketListener(channel)

        val userId = getUserId()

        val websocket = okHttpClient.newWebSocket(
            Request.Builder().url("$WEBSOCKET_URL/$endpoint/$userId/").build(),
            listener
        )

        awaitClose { websocket.close(1001, "Listener closed") }
    }

    private fun <T> CoroutineScope.webSocketListener(channel: SendChannel<T>) =
        object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val receivedValue: T = gson.fromJson(text, object : TypeToken<T>() {}.type)

                Log.d("UndoTag", "Received from socket: $text")

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
}