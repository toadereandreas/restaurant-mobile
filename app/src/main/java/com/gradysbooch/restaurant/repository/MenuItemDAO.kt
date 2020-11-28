package com.gradysbooch.restaurant.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.gradysbooch.restaurant.model.MenuItem
import com.gradysbooch.restaurant.model.MenuItemWithOrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDAO
{
    @Transaction
    suspend fun updateMenu(menuItems: Set<MenuItem>)
    {
        if (getMenu().toSet() != menuItems)
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

    @Transaction
    @Query("SELECT MenuItem.* FROM MenuItem JOIN OrderItem ON MenuItem.menuItemUID=OrderItem.menuItemUID WHERE OrderItem.tableUID=:tableUID")
    fun getMenuItemsForTable(tableUID: String): Flow<List<MenuItemWithOrderItem>>
}