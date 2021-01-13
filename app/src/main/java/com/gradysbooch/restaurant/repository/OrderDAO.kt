package com.gradysbooch.restaurant.repository

import androidx.room.*
import com.gradysbooch.restaurant.model.Order
import com.gradysbooch.restaurant.model.OrderItem
import com.gradysbooch.restaurant.model.OrderItemWithMenuItem
import com.gradysbooch.restaurant.model.OrderWithMenuItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDAO
{
    @Transaction
    @Query("SELECT * FROM `Order` WHERE tableUID=:tableUID AND orderColor=:color")
    fun getOrderWithMenuItems(tableUID: String, color: String): Flow<OrderWithMenuItems?>

    @Query("SELECT * FROM `Order`")
    suspend fun getOrders(): List<Order>

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

    @Query("DELETE FROM `Order` WHERE tableUID=:tableUID")
    suspend fun clearOrders(tableUID: String)

    @Query("DELETE FROM OrderItem WHERE tableUID=:tableUID")
    suspend fun clearOrderItems(tableUID: String)

    @Transaction
    suspend fun clearTable(tableUID: String)
    {
        clearOrders(tableUID)
        clearOrderItems(tableUID)
    }

    @Transaction
    suspend fun saveOrderItems(orderItems: List<OrderItem>)
    {
        getOrders().groupBy { it.tableUID }.forEach { (tableUID, orders) ->
            deleteOrderItems(tableUID, excludedColors = orders.map { it.orderColor })
        }
        insertOrderItems(orderItems)
    }

    @Query("DELETE FROM OrderItem WHERE tableUID=:tableUID AND orderColor NOT IN (:excludedColors)")
    suspend fun deleteOrderItems(tableUID: String, excludedColors: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(orderItems: List<OrderItem>)

    @Query("DELETE FROM `Order` WHERE tableUID=:tableUID AND orderColor=:color")
    suspend fun deleteOrder(tableUID: String, color: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addOrderItem(orderItem: OrderItem)

    @Transaction
    @Query("SELECT * FROM `Order` WHERE tableUID=:tableUID AND orderColor=:color")
    suspend fun orderWithMenuItems(tableUID: String, color: String) : OrderWithMenuItems?

}