package com.gradysbooch.restaurant.repository.networkRepository

import com.gradysbooch.restaurant.model.*
import kotlinx.coroutines.flow.Flow

interface NetworkRepositoryInterface
{
    val onlineStatus: Flow<Boolean>

    suspend fun getMenuItems(): Set<MenuItem>

    fun getTables(): Flow<Set<Table>>

    fun clientOrders(): Flow<List<Order>>

    fun orderItems(): Flow<List<OrderItem>>

    suspend fun clearCall(tableUID: String)

    suspend fun updateOrder(orderWithMenuItems: OrderWithMenuItems)

    suspend fun unlockOrder(tableUID: String, color: String)

    suspend fun lockOrder(tableUID: String, color: String)

    suspend fun login(email: String, password: String) : String
}