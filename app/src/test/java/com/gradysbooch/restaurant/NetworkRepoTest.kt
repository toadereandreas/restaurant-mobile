package com.gradysbooch.restaurant

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.gradysbooch.restaurant.repository.networkRepository.NetworkRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class NetworkRepoTest {

    val apolloClient = ApolloClient.builder()
            .serverUrl("https://restaurant.playgroundev.com/graphql/")
            .build()

    @Test
    fun testApolloUsingDummy(){

        var q : GetMenuItemsQuery.MenuItems? = null;

        try {
            runBlocking {
                q = apolloClient
                        .query(
                                GetMenuItemsQuery()
                        ).await().data?.menuItems;
            }
        }
        catch (e : ApolloException){
            println(e);
        }

        println(q.toString());

    }

    lateinit var instrumentationContext: Context

    @Before
    fun setup(){
        //instrumentationContext = InstrumentationRegistry.getContext()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun TestBasicFlow(){

    }
}