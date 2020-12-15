package com.gradysbooch.restaurant.repository

import androidx.room.*
import com.gradysbooch.restaurant.model.MenuItem
import com.gradysbooch.restaurant.model.MenuItemWithOrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuItemDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMenu(menuItems: Set<MenuItem>)

    @Query("DELETE FROM MenuItem")
    suspend fun deleteMenu()

    @Query("SELECT * FROM MenuItem")
    fun getMenuFlow(): Flow<List<MenuItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenu(menuItems: List<MenuItem>)

    @Transaction
    @Query("SELECT DISTINCT MenuItem.* FROM MenuItem JOIN OrderItem ON MenuItem.menuItemUID=OrderItem.menuItemUID WHERE OrderItem.tableUID=:tableUID")
    fun getMenuItemsForTable(tableUID: String): Flow<List<MenuItemWithOrderItem>>
}