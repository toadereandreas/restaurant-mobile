package com.gradysbooch.restaurant.repository

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.gradysbooch.restaurant.model.OrderWithMenuItems
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO
{
    @Transaction
    @Query("SELECT * FROM `Order`")
    fun getOrdersWithMenuItems(): Flow<List<OrderWithMenuItems>>
}