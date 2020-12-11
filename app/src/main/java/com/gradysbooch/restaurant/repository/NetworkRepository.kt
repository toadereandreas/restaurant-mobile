package com.gradysbooch.restaurant.repository

import android.content.Context
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toFlow
import com.apollographql.apollo.exception.ApolloException
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport
import com.gradysbooch.restaurant.GetMenuItemsQuery
//import com.gradysbooch.restaurant.SubscribeToOrderItemsSubscription
//import com.gradysbooch.restaurant.SubscribeToOrdersSubscription
//import com.gradysbooch.restaurant.SubscribeToTablesSubscription
import com.gradysbooch.restaurant.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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
        .addInterceptor(BlankInterceptor(context))
        .build()

    val apolloClient = ApolloClient.builder()
        .serverUrl(
            "https://restaurant.playgroundev.com/graphql/"
            //"http://halex193.go.ro:8000/graphql/"
        )
        .subscriptionTransportFactory(WebSocketSubscriptionTransport.Factory("ws://restaurant.playgroundev.com/graphql/", okHttpClient))
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
//
//        return list.filterNotNull().map {
//            MenuItem(it.id
//                    ?: error("Id null."),
//                    it.internalName,
//                    it.price.roundToInt())
//        }.toSet()
        return HashSet()
    }

    @ExperimentalCoroutinesApi
    override fun getTables(): Flow<Set<Table>> {
//        return apolloClient.subscribe(SubscribeToTablesSubscription())
//                .toFlow()
//                .map { value ->
//                    value.data?.servings?.data?.map { it ->
//                        it ?: error("Item null");
//                        Table(
//                                it.userId ?: error("UserId null"),
//                                "PLACEHOLDER",
//                                it.code?.toInt(),
//                                it.called ?: false
//                        )
//                    }?.toSet() ?: error("Set null")
//                }
        //theoretically, I'm transforming the values of this flow in the following manner:
        // grab the innermost relevant data and map every instance of dto item
        // and map it to a table object then collect it in a set...
        //todo fix placeholder, look for alternatives for the null safety checks
        return emptyFlow()
    }

    @ExperimentalCoroutinesApi
    override fun clientOrders(): Flow<List<Order>> {
//        return apolloClient.subscribe(SubscribeToOrdersSubscription())
//                .toFlow()
//                .map { value ->
//                    value.data?.orders?.data?.map { it ->
//                        it ?: error("Item null");
//                        Order(
//                                it.id ?: error("Id null"),
//                                "PLACEHOLDER",
//                                it.note ?: error("Note null")
//                        )
//                    }?.toList() ?: error("List null")
//                }
        return emptyFlow()
    }

    @ExperimentalCoroutinesApi
    override fun orderItems(): Flow<List<OrderItem>> {
//        return apolloClient.subscribe(SubscribeToOrderItemsSubscription())
//                .toFlow()
//                .map { value ->
//                    value.data?.orderMenuItems?.data?.map { it ->
//                        it ?: error("Item null")
//                        OrderItem(
//                                it.color ?: error("Color null"),
//                                it.servingId ?: error("ServingId null"),
//                                it.menuItemId ?: error("MenuItemId null"),
//                                it.quantity ?: error("Quality null")
//                        )
//                    }?.toList() ?: error("List null")
//                }
        return emptyFlow()
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
}