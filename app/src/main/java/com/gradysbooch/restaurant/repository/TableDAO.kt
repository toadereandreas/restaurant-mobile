package com.gradysbooch.restaurant.repository

import androidx.room.*
import com.gradysbooch.restaurant.model.Table
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDAO
{
    @Query("SELECT * FROM `Table`")
    fun getTables(): Flow<List<Table>>

    @Query("SELECT * FROM `Table` WHERE UID=:tableUID")
    fun getTable(tableUID: String): Flow<Table?>

    @Query("UPDATE `Table` SET call=:call WHERE UID=:tableUID")
    suspend fun updateTableCall(tableUID: String, call: Boolean)
}