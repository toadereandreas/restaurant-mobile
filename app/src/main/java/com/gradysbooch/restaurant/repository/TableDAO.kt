package com.gradysbooch.restaurant.repository

import androidx.room.*
import com.gradysbooch.restaurant.model.Table
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDAO
{
    @Query("SELECT * FROM `Table`")
    fun getTables(): Flow<List<Table>>

    @Query("SELECT * FROM `Table` WHERE tableUID=:tableUID")
    fun getTable(tableUID: String): Flow<Table?>

    @Query("UPDATE `Table` SET call=:call WHERE tableUID=:tableUID")
    suspend fun updateTableCall(tableUID: String, call: Boolean)

    @Query("UPDATE `Table` SET code=null WHERE tableUID=:tableUID")
    suspend fun clearTable(tableUID: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTables(tables: List<Table>)
}