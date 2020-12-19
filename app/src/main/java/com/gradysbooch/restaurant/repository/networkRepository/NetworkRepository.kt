package com.gradysbooch.restaurant.repository.networkRepository

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gradysbooch.restaurant.GetMenuItemsQuery
import com.gradysbooch.restaurant.LoginMutation
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
import okhttp3.*
import okio.ByteString.Companion.decodeBase64
import kotlin.math.roundToInt

private const val WEBSOCKET_URL = "ws://restaurant.playgroundev.com:5000/ws"
private const val GRAPHQL_URL = "http://restaurant.playgroundev.com/graphql/"
private const val EMAIL = "admin@welcome.com"
private const val PASSWORD = "welcome"
//"http://restaurant.playgroundev.com/graphql/"
//"http://halex193.go.ro:8000/graphql/"

class NetworkRepository(context: Context) : NetworkRepositoryInterface {
    private val gson = Gson()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val internalOnlineStatus = MutableStateFlow(false) //todo implement proper online status tracking
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

    override fun getTables(): Flow<Set<Table>> = subscribe("serving")

    override fun clientOrders(): Flow<List<Order>> = subscribe("order")

    override fun orderItems(): Flow<List<OrderItem>> = subscribe("ordermenuitem")

    override suspend fun clearCall(tableUID: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrder(orderWithMenuItems: OrderWithMenuItems) {
        TODO("Not yet implemented")
    }

    override suspend fun unlockOrder(tableUID: String, color: String) {
        TODO("Not yet implemented")
    }

    override suspend fun lockOrder(tableUID: String, color: String) {
        TODO("Not yet implemented")
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
    }

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