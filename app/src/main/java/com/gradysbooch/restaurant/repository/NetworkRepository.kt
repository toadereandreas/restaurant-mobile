package com.gradysbooch.restaurant.repository

import android.content.Context
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.Logger
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.subscription.OperationClientMessage
import com.apollographql.apollo.subscription.OperationServerMessage
import com.apollographql.apollo.subscription.SubscriptionTransport
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import com.gradysbooch.restaurant.GetMenuItemsQuery
import com.gradysbooch.restaurant.SubscribeToOrderItemsSubscription
import com.gradysbooch.restaurant.SubscribeToOrdersSubscription
import com.gradysbooch.restaurant.SubscribeToTablesSubscription
import com.gradysbooch.restaurant.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import okhttp3.*
import okio.ByteString
import java.io.IOException
import kotlin.math.roundToInt

private class BlankInterceptor(val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .build()

        return chain.proceed(request)
    }
}

class NetworkRepository(context: Context) : NetworkRepositoryInterface {

    val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(BlankInterceptor(context))
        .build()

    val apolloClient = ApolloClient.builder()
        .serverUrl(
            //"https://restaurant.playgroundev.com/graphql/"
            "http://halex193.go.ro:8000/graphql/"
        )
        .subscriptionTransportFactory(apolloSubTransportFactory())
        .okHttpClient(okHttpClient)
        .build()

    @OptIn(ExperimentalCoroutinesApi::class)
    val internalOnlineStatus = MutableStateFlow(false)

    override val onlineStatus: Flow<Boolean> = internalOnlineStatus

    /**
     * This function does an apollo call and checks for apollo failure and null return.
     * @param T
     * The return type, must be Operation.Data. It is up to the caller to make sure that the query returns the propper return type
     * @param GQLQuery
     *  Instance of the query to be run
     */
    private suspend inline fun <reified T : Operation.Data> runQuerySafely(GQLQuery: Query<*, *, *>): T {
        try {
            val result = (apolloClient.query(GQLQuery).await().data as? T
                    ?: throw IOException("ApolloFailure: menu items returned null."))

            internalOnlineStatus.emit(true)
            return result

        } catch (e: ApolloException) {
            Log.d("NetworkError", e.stackTraceToString())

            internalOnlineStatus.emit(false)
            throw e
        }
    }

    override suspend fun getMenuItems(): Set<MenuItem> {
        val list = runQuerySafely<GetMenuItemsQuery.Data>(GetMenuItemsQuery()).menuItems?.data
                ?: throw IOException("ApolloFailure: menu items returned null.")

        return list.filterNotNull().map {
            MenuItem(it.id
                    ?: error("Id null."),
                    it.internalName,
                    it.price.roundToInt())
        }.toSet()
    }

    @ExperimentalCoroutinesApi
    override fun getTables(): Flow<Set<Table>> {
        return apolloClient.subscribe(SubscribeToTablesSubscription())
                .toFlow()
                .onEach { Log.d("NetworkRepository", it.toString()) }
                .map {setOf(Table(it.data?.hello!!, "", 0, true))}
    }

    @ExperimentalCoroutinesApi
    override fun clientOrders(): Flow<List<Order>> {
        return apolloClient.subscribe(SubscribeToOrdersSubscription())
                .toFlow()
                .map { value ->
                    value.data?.orders?.data?.map { it ->
                        it ?: error("Item null");
                        Order(
                                it.id ?: error("Id null"),
                                "PLACEHOLDER",
                                it.note ?: error("Note null")
                        )
                    }?.toList() ?: error("List null")
                }
    }

    @ExperimentalCoroutinesApi
    override fun orderItems(): Flow<List<OrderItem>> {
        return apolloClient.subscribe(SubscribeToOrderItemsSubscription())
                .toFlow()
                .map { value ->
                    value.data?.orderMenuItems?.data?.map { it ->
                        it ?: error("Item null")
                        OrderItem(
                                it.color ?: error("Color null"),
                                it.servingId ?: error("ServingId null"),
                                it.menuItemId ?: error("MenuItemId null"),
                                it.quantity ?: error("Quality null")
                        )
                    }?.toList() ?: error("List null")
                }
    }

    override suspend fun clearCall(tableUID: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrder(orderWithMenuItems: OrderWithMenuItems) {
        TODO("Not yet implemented")
    }

    override suspend fun unlockOrder(tableUID: String, color: String)
    {
        TODO("Not yet implemented")
    }

    override suspend fun lockOrder(tableUID: String, color: String)
    {
        TODO("Not yet implemented")
    }
    fun apolloSubTransportFactory(): SubscriptionTransport.Factory {
        return SubscriptionTransport.Factory { callback ->
            val wsListener = object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                    callback.onConnected()
                    super.onOpen(webSocket, response)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    callback.onClosed()
                    super.onClosed(webSocket, code, reason)
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    callback.onClosed()
                    super.onClosing(webSocket, code, reason)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                    Logger.d("WebSocketListener::onFailure: ${t.localizedMessage}")
                    callback.onFailure(t)
                    super.onFailure(webSocket, t, response)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    callback.onMessage(OperationServerMessage.fromJsonString(text))
                    super.onMessage(webSocket, text)
                }

                override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                    super.onMessage(webSocket, bytes)
                }
            }

            object : SubscriptionTransport {
                fun req(): Request {
                    val json = sharedPrefs.get(KeyValModel.Key.USER_AUTH.value, "")
                    return try {
                        if (json.isNotBlank()) {
                            val model = UserAuthModel.fromJsonString(json)
                            Request.Builder()
                                    .url(BuildConfig.netGraphQLUrlWss)
                                    .addHeader("Authorization", "Bearer ${model.tokenAccess ?: ""}")
                                    .addHeader("X-Hasura-Role", model.userRole.value)
                                    .addHeader("Sec-WebSocket-Protocol", "graphql-ws")
                                    .addHeader("Cookie", "")
                                    .build()
                        } else {
                            Logger.d("UserAuthModel JSON was empty, skipping...")
                            Request.Builder()
                                    .url(BuildConfig.netGraphQLUrlWss)
                                    .addHeader("Sec-WebSocket-Protocol", "graphql-ws")
                                    .addHeader("Cookie", "")
                                    .build()
                        }
                    } catch (t: Throwable) {
                        Logger.e(t.localizedMessage ?: "(NULL)")

                        Request.Builder()
                                .url(BuildConfig.netGraphQLUrlWss)
                                .addHeader("Sec-WebSocket-Protocol", "graphql-ws")
                                .addHeader("Cookie", "")
                                .build()
                    }
                }

                val ws: WebSocket by lazy { okHttpClient.newWebSocket(req(), wsListener) }

                override fun connect() {
                    //Logger.d("SubscriptionTransport.connect(): Called, connecting WebSocket...")
                    ws
                }

                override fun disconnect(message: OperationClientMessage?) {
                    //Logger.d(message?.toJsonString())
                    ws.close(1001, message?.toJsonString())
                }

                override fun send(message: OperationClientMessage?) {
                    if (message != null) {
                        //Logger.d(message.toJsonString())
                        ws.send(message.toJsonString())
                    } else {
                        Logger.w("SubscriptionTransport.send(message): message was null")
                    }
                }
            }
        }
    }
}