package com.gradysbooch.restaurant.repository

import android.content.Context
import com.gradysbooch.restaurant.model.MenuItem
import com.gradysbooch.restaurant.model.Order
import com.gradysbooch.restaurant.model.Table
import com.gradysbooch.restaurant.model.dto.Bullet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow

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

    override fun clientOrders(): Flow<List<Order>> {
        return emptyFlow()
    }
}