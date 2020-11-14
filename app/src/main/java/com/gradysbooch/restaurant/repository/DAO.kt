package com.gradysbooch.restaurant.repository

import androidx.room.*
import com.gradysbooch.restaurant.model.MenuItem
import com.gradysbooch.restaurant.model.OrderWithMenuItems
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO
{
    @Transaction
    @Query("SELECT * FROM `Order`")
    fun getOrdersWithMenuItems(): Flow<List<OrderWithMenuItems>>

    @Transaction
    suspend fun updateMenu(menuItems: Set<MenuItem>)
    {
        if(getMenu().toSet() != menuItems)
        {
            deleteMenu()
            insertMenu(menuItems.toList())
        }
    }

    @Query("DELETE FROM MenuItem")
    suspend fun deleteMenu()

    @Query("SELECT * FROM MenuItem")
    suspend fun getMenu(): List<MenuItem>

    @Insert
    suspend fun insertMenu(menuItems: List<MenuItem>)
}