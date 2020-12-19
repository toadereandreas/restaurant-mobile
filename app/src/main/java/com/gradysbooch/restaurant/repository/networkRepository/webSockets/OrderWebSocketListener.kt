package com.gradysbooch.restaurant.repository.networkRepository.webSockets

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gradysbooch.restaurant.model.Order
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class OrderWebSocketListener(val internalOnlineStatus: MutableStateFlow<Boolean>) : WebSocketListener(){
    private val gson = Gson()
    private val privateFlow : MutableStateFlow<List<Order>> = MutableStateFlow(listOf())

    fun getFlow() : Flow<List<Order>> {
        return privateFlow.asStateFlow()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("UndoTag", text)

        val receivedValue = gson.fromJson<ArrayList<Order>>(text, object: TypeToken<ArrayList<Order>>(){}.type)

        GlobalScope.launch {
            privateFlow.emit(receivedValue)
            internalOnlineStatus.emit(true)
        }
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("UndoTag", t.stackTraceToString())
        runBlocking { internalOnlineStatus.emit(false) }
    }
}