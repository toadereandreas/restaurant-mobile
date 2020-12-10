package com.gradysbooch.restaurant.repository

import androidx.room.*
import com.gradysbooch.restaurant.model.Table
import kotlinx.coroutines.flow.Flow

@Dao
interface TableDAO
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTable(table: Table)

    @Query("SELECT * FROM `Table`")
    fun getTables(): Flow<List<Table>>

    @Query("SELECT * FROM `Table` WHERE tableUID=:tableUID")
    fun getTable(tableUID: String): Flow<Table?>

    @Query("UPDATE `Table` SET call=:call WHERE tableUID=:tableUID")
    suspend fun updateTableCall(tableUID: String, call: Boolean)

    @Query("UPDATE `Table` SET code=null WHERE tableUID=:tableUID")
    suspend fun clearTable(tableUID: String)

    @Delete
    suspend fun deleteTable(table: Table)
}