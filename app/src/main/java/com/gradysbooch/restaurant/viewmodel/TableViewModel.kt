package com.gradysbooch.restaurant.viewmodel

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.lifecycle.viewModelScope
import com.gradysbooch.restaurant.model.Notification
import com.gradysbooch.restaurant.model.dto.TableDTO
import com.gradysbooch.restaurant.model.dto.toDTO
import com.gradysbooch.restaurant.notifications.NotificationReceiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@RequiresApi(Build.VERSION_CODES.M)
class TableViewModel(application: Application) : BaseViewModel(application),
    TableViewModelInterface {
    init {
        val channelId = "TABLE_NOTIFICATION"
        NotificationReceiver.createNotificationChannel(
            channelId,
            "Calls from clients",
            "Calls from customers regarding specific tables",
            application.applicationContext.getSystemService()!!
        )
        repository.networkRepository.getTables()
            .onEach { tables ->
                repository.tableDao().updateTables(tables.sortedBy { it.tableUID })
                val log = tables.joinToString(", ") {"${it.name}-${it.call}" }
                Log.d("TableViewModel", "Received tables: $log")
                tables.filter { it.call }
                    .forEach {
                        NotificationReceiver.sendNotification(
                            application.applicationContext,
                            channelId,
                            Notification(
                                it.tableUID.hashCode(),
                                "Ești chemat la ${it.name}",
                                it.tableUID
                            )
                        )
                    }
            }
            .launchIn(viewModelScope)
    }

    override val tables: Flow<List<TableDTO>> =
        repository.tableDao()
            .getTables()
            .map { tables ->
                tables.map { it.toDTO() }
            }
}