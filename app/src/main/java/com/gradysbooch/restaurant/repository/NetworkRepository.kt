package com.gradysbooch.restaurant.repository

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.gradysbooch.restaurant.GetMenuItemDetailsQuery

import kotlinx.coroutines.runBlocking

val apolloClient = ApolloClient.builder()
    .serverUrl("http://restaurant-env.eba-prznaj7v.eu-west-3.elasticbeanstalk.com/graphql/")
    .build()

class NetworkRepository(context: Context) : NetworkRepositoryInterface
{
    @OptIn(ExperimentalCoroutinesApi::class)
    val internalOnlineStatus = MutableStateFlow(false)

    override val onlineStatus: Flow<Boolean> = internalOnlineStatus

    /**
     * This is a test function
     * Murderize it when you don't need it anymore
     *
     * This might return null, watch out
     */
    suspend fun getMenuItem(id: Int) : GetMenuItemDetailsQuery.MenuItem?{
        val menuItem : GetMenuItemDetailsQuery.MenuItem?;

        menuItem = apolloClient.query(GetMenuItemDetailsQuery(id.toString())).toDeferred().await().data?.menuItem


        return menuItem;
    }
}