package com.gradysbooch.restaurant.ui.screens.notification

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import com.gradysbooch.restaurant.MainActivity
import com.gradysbooch.restaurant.model.Notification
import java.time.LocalDate
import kotlin.coroutines.coroutineContext

@Composable
fun NotificationScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NotificationButton()
    }
}

@Composable
fun NotificationButton() {
    Button(onClick = {

    }, backgroundColor = Color.White) {
        Text(text = "Send Notification", color = Color.Black)
    }
}

