package com.gradysbooch.restaurant.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gradysbooch.restaurant.R
import com.gradysbooch.restaurant.model.Notification

class NotificationReceiver {

    companion object {
        fun createNotificationChannel(id: String,
                                      name: String,
                                      descriptionText: String,
                                      notificationManager: NotificationManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(id, name, importance).apply {
                    description = descriptionText
                }
                notificationManager.createNotificationChannel(channel)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun removeNotificationChannel(id: String,
                                      notificationManager: NotificationManager) {
            notificationManager.deleteNotificationChannel(id)
        }

        fun sendNotification(context: Context, channelId: String, notification: Notification) {
            val builder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle("New Announcement")
                    .setContentText(notification.description)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
            with(NotificationManagerCompat.from(context)) {
                notify(notification.id, builder.build())
            }
        }
    }
}