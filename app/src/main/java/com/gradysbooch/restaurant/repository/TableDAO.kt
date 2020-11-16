package com.gradysbooch.restaurant.repository

import androidx.room.*
import com.gradysbooch.restaurant.model.MenuItem
import com.gradysbooch.restaurant.model.OrderWithMenuItems
import com.gradysbooch.restaurant.model.Table
import com.gradysbooch.restaurant.model.TableWithOrders
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDAO
{
    @Query("SELECT * FROM `Table`")
    fun getTableFlow(): Flow<List<Table>>

    @Transaction
    @Query("SELECT * FROM `Table` WHERE UID=:tableUID")
    fun getTableWithOrdersFlow(tableUID: String): Flow<TableWithOrders>
}