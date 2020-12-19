package com.gradysbooch.restaurant.repository.networkRepository

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.gradysbooch.restaurant.GetMenuItemsQuery
import com.gradysbooch.restaurant.LoginMutation
import com.gradysbooch.restaurant.model.*
import com.gradysbooch.restaurant.repository.networkRepository.webSockets.OrderItemWebSocketListener
import com.gradysbooch.restaurant.repository.networkRepository.webSockets.OrderWebSocketListener
import com.gradysbooch.restaurant.repository.networkRepository.webSockets.TableWebSocketListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.math.log
import kotlin.math.roundToInt

class NetworkRepository(context: Context, email: String, password: String) : NetworkRepositoryInterface {

    private lateinit var USER_TOKEN : String
    private lateinit var ORDER_ITEM_WEBSOCKET_URL : String
    private lateinit var ORDER_WEBSOCKET_URL : String
    private lateinit var TABLE_WEBSOCKET_URL : String
    private val GRAPHQL_URL = "http://restaurant.playgroundev.com/graphql/"
    //"http://restaurant.playgroundev.com/graphql/"
    //"http://halex193.go.ro:8000/graphql/"

    private val okHttpClient = OkHttpClient.Builder().build()
    //note-to-self: This guy's threads and connections are released automatically, no need to deal with that

    private val apolloClient = ApolloClient.builder().serverUrl(GRAPHQL_URL).build()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val internalOnlineStatus = MutableStateFlow(false)
    override val onlineStatus: Flow<Boolean> = internalOnlineStatus

    private val orderItemWebSocketListener = OrderItemWebSocketListener(internalOnlineStatus)
    private val orderWebSocketListener = OrderWebSocketListener(internalOnlineStatus)
    private val tableWebSocketListener = TableWebSocketListener(internalOnlineStatus)

    init {
        runBlocking {
            //The plan here is to run the login request, and setup the links before actually setting up the sockets
            //albeit, this might be inconsequential due to the fact that I'm sending variables to those constructors, but whatever

            val login = apolloClient.mutate(LoginMutation(Input.fromNullable(email), password)).await().data?.tokenAuth ?: error("ApolloFailure: login is null.")
            val userID = login.user?.id ?: error("ApolloFailure: user id null")

            USER_TOKEN = login.token ?: error("ApolloFailure: login token null")
            ORDER_WEBSOCKET_URL = 

        }

        okHttpClient.newWebSocket(
            Request.Builder().url(ORDER_ITEM_WEBSOCKET_URL).build(),
            orderItemWebSocketListener
        )
        okHttpClient.newWebSocket(
            Request.Builder().url(ORDER_WEBSOCKET_URL).build(),
            orderWebSocketListener
        )
        okHttpClient.newWebSocket(
            Request.Builder().url(TABLE_WEBSOCKET_URL).build(),
            tableWebSocketListener
        )
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

    override suspend fun getMenuItems(): Set<MenuItem> {
        val list = runQuerySafely<GetMenuItemsQuery.Data>(GetMenuItemsQuery()).menuItems?.data
            ?: error("ApolloFailure: menu items returned null.")

        return list.filterNotNull().map { MenuItem(it.id.toString(), it.internalName, it.price.roundToInt()) }.toSet()
    }

    override fun getTables(): Flow<Set<Table>> {
        return tableWebSocketListener.getFlow()
    }

    override fun clientOrders(): Flow<List<Order>> {
        return orderWebSocketListener.getFlow()
    }

    override fun orderItems(): Flow<List<OrderItem>> {
        return orderItemWebSocketListener.getFlow()
    }

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
}