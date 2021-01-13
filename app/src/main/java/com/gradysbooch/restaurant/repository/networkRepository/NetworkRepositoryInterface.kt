package com.gradysbooch.restaurant.repository.networkRepository

import com.gradysbooch.restaurant.model.*
import kotlinx.coroutines.flow.Flow

interface NetworkRepositoryInterface
{
    val onlineStatus: Flow<Boolean>

    suspend fun getMenuItems(): Set<MenuItem>

    fun getTables(): Flow<List<Table>>

    fun clientOrders(): Flow<List<Order>>

    fun orderItems(): Flow<List<OrderItem>>

    suspend fun clearCall(tableUID: String)

    @Deprecated("We stopped using this. When? Just now!")
    suspend fun updateOrder(orderWithMenuItems: OrderWithMenuItems)

    suspend fun unlockOrder(tableUID: String, color: String)

    suspend fun lockOrder(tableUID: String, color: String)

    suspend fun clearTable(tableUID: String)

    suspend fun createOrderItem(orderItem: OrderItem)

    suspend fun updateOrderItem(orderItem: OrderItem)

    suspend fun createOrder(tableUID: String, color: String)
}