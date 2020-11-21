package com.gradysbooch.restaurant.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.gradysbooch.restaurant.model.Order
import com.gradysbooch.restaurant.model.OrderWithMenuItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDAO
{
    @Transaction
    @Query("SELECT * FROM `Order` WHERE tableUID=:tableUID AND orderColor=:color")
    fun getOrderWithMenuItems(tableUID: String, color: String): Flow<OrderWithMenuItems?>

    @Transaction
    @Query("SELECT * FROM `Order` WHERE tableUID=:tableUID")
    fun getOrdersWithMenuItems(tableUID: String): Flow<List<OrderWithMenuItems>>


    @Query("SELECT * FROM `Order` WHERE tableUID=:tableUID")
    fun getOrdersForTable(tableUID: String): Flow<List<Order>>

    @Query("SELECT * FROM `Order` WHERE tableUID=:tableUID AND orderColor=:color")
    suspend fun getOrder(tableUID: String, color: String): Order?

    @Insert
    suspend fun addOrder(order: Order)

    @Query("UPDATE `Order` SET note=:note WHERE tableUID=:tableUID AND orderColor=:color")
    suspend fun updateNote(tableUID: String, color: String, note: String)

    @Query("UPDATE OrderItem SET quantity=:quantity WHERE tableUID=:tableUID AND orderColor=:color AND menuItemUID=:menuItemId")
    suspend fun changeNumber(tableUID: String, color: String, menuItemId: String, quantity: Int)
}