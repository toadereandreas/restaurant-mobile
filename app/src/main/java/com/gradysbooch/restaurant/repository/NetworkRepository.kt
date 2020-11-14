package com.gradysbooch.restaurant.repository

import android.content.Context
import com.gradysbooch.restaurant.model.MenuItem
import com.gradysbooch.restaurant.model.Table
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class NetworkRepository(context: Context) : NetworkRepositoryInterface
{
    @OptIn(ExperimentalCoroutinesApi::class)
    val internalOnlineStatus = MutableStateFlow(false)

    override val onlineStatus: Flow<Boolean> = internalOnlineStatus

    override suspend fun getMenuItems(): Set<MenuItem> {
        TODO("Not yet implemented")
    }

    override suspend fun getTables(): Set<Table> {
        TODO("Not yet implemented")
    }
}