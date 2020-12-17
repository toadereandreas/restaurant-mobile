package com.gradysbooch.restaurant.repository.networkRepository.webSockets

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gradysbooch.restaurant.model.OrderItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class OrderItemWebSocketListener(val internalOnlineStatus: MutableStateFlow<Boolean>) : WebSocketListener(){
    private val gson = Gson()
    private val privateFlow : MutableStateFlow<List<OrderItem>> = MutableStateFlow(listOf())

    fun getFlow() : Flow<List<OrderItem>> {
        return privateFlow.asStateFlow()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val receivedValue = gson.fromJson<ArrayList<OrderItem>>(text, object: TypeToken<ArrayList<OrderItem>>(){}.type)

        GlobalScope.launch {
            /*
            I checked the websocket doc https://www.javadoc.io/doc/org.java-websocket/Java-WebSocket/1.3.5/org/java_websocket/WebSocketListener.html

            and couldn't find anything about how these threads are run (comon sense tells me that every callback takes place on a separate thread
            I know there's at least one separate thread (probably okhttpclient's executor service), but I don't know if there's one for each callback, so I'm using a coroutine instead
            of run blocking.

            todo solve this mistery
            */
            privateFlow.emit(receivedValue)
            internalOnlineStatus.emit(true)
            //could I have called this from an onConnect callback? yes.
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("UndoTag", t.stackTraceToString())
        runBlocking { internalOnlineStatus.emit(false) }
    }
}