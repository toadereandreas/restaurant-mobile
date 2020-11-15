package com.gradysbooch.restaurant.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.gradysbooch.restaurant.GetMenuItemDetailsQuery
import com.gradysbooch.restaurant.GetMenuItemsQuery
import com.gradysbooch.restaurant.model.MenuItem
import com.gradysbooch.restaurant.model.Table
import java.io.IOException
import kotlin.math.log
import kotlin.math.roundToInt

val apolloClient = ApolloClient.builder()
    .serverUrl("https://restaurant.playgroundev.com/graphql")
    .build()

class NetworkRepository(context: Context) : NetworkRepositoryInterface
{
    @OptIn(ExperimentalCoroutinesApi::class)
    val internalOnlineStatus = MutableStateFlow(false)

    override val onlineStatus: Flow<Boolean> = internalOnlineStatus

    override suspend fun getMenuItems(): Set<MenuItem> {
        val set = mutableSetOf<MenuItem>();

        try{
            val list = apolloClient.query(GetMenuItemsQuery()).await().data?.menuItems?.data;

            var badId = 0;//todo Fix this badId thing. it should be received from the backend
            list?.forEach {
                if (it != null)
                    set.add(MenuItem(badId++, it.internalName, it.category.internalName, it.price.roundToInt()))
            }

            internalOnlineStatus.emit(true);

        }catch (e : ApolloException){
            internalOnlineStatus.emit(true);
            Log.d("ApolloError", e.stackTraceToString());

            throw IOException("ApolloFailure: failed to get menu items. Exception is: ${e.stackTraceToString()}");
        }

        return set;
    }

    override suspend fun getTables(): Set<Table> {
        TODO("Not yet implemented")
    }

    /**
     * This is a test function
     * todo Test and murderize it when you don't need it anymore
     *
     * This might return null, watch out
     */
    suspend fun getMenuItem(id: Int) : GetMenuItemDetailsQuery.MenuItem?{

        return apolloClient.query(GetMenuItemDetailsQuery(id.toString())).await().data?.menuItem

    }
}