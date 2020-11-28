package com.gradysbooch.restaurant.repository

import androidx.room.*
import com.gradysbooch.restaurant.model.MenuItem
import com.gradysbooch.restaurant.model.OrderWithMenuItems
import com.gradysbooch.restaurant.model.Table
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

    @Query("SELECT * FROM MenuItem")
    fun getMenuFlow(): Flow<List<MenuItem>>

    @Insert
    suspend fun insertMenu(menuItems: List<MenuItem>)

    @Query("SELECT * FROM `Table`")
    fun getTableFlow(): Flow<List<Table>>

    @Transaction
    @Query("SELECT * FROM `Order` WHERE orderId=:tableId") //todo Andu a schimbat tableId in orderId, deoarece nu putea sa dea build. Must fix after foreign key is added
    fun getOrdersWithMenuItemsForTable(tableId: Int): Flow<List<OrderWithMenuItems>>
}