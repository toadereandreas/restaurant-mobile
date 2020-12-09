package com.gradysbooch.restaurant

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.gradysbooch.restaurant.model.MenuItem
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.IOException
import java.lang.NullPointerException
import kotlin.math.roundToInt

class NetworkRepoTest {

    val apolloClient = ApolloClient.builder()
            .serverUrl("https://restaurant.playgroundev.com/graphql/")
            .build()

    private suspend fun <T : Operation.Data>runQuerySafely(GQLQuery : Query<*, *, *>): T {
        try{
            val result = (com.gradysbooch.restaurant.repository.apolloClient.query(GQLQuery).await().data
                    ?: throw NullPointerException("got null")) as T

            return result;

        }catch (e : ApolloException){
            Log.d("NetworkError", e.stackTraceToString());

            throw IOException("ApolloFailure: failed to get menu items. Exception is: ${e.stackTraceToString()}");
        }
        catch (e : NullPointerException){
            Log.d("NetworkError", e.stackTraceToString());
            throw IOException("ApolloFailure: menu items returned null. Exception is: ${e.stackTraceToString()}");
        }

    }

    //val context = ApplicationProvider.getApplicationContext<Context>()
    //val networkRepository :NetworkRepository = NetworkRepository(context);

    @Test
    fun testRunQuerySafely() = runBlocking{
        //todo fix networkRepo constructor usages
        val list = runQuerySafely<GetMenuItemsQuery.Data>(GetMenuItemsQuery()).menuItems

        assert(list != null)
    }

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