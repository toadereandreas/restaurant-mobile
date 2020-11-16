package com.gradysbooch.restaurant.repository

import androidx.room.Query
import androidx.room.Transaction
import com.gradysbooch.restaurant.model.OrderWithMenuItems
import kotlinx.coroutines.flow.Flow

interface OrderDAO
{
    @Transaction
    @Query("SELECT * FROM `Order` WHERE tableUID=:tableUID")
    fun getOrderWithMenuItems(tableUID: String): Flow<OrderWithMenuItems>
}