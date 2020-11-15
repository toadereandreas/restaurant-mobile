package com.gradysbooch.restaurant

//import android.content.Context
//import com.gradysbooch.restaurant.repository.NetworkRepository
//import androidx.test.core.app.ApplicationProvider
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import kotlinx.coroutines.runBlocking
import org.junit.Test

class NetworkRepoTest {

    //val context = ApplicationProvider.getApplicationContext<Context>()
    //val networkRepository :NetworkRepository = NetworkRepository(context);

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
}