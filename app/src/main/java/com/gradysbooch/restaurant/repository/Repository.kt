package com.gradysbooch.restaurant.repository

import android.content.Context
import androidx.room.Room

fun buildRoomDB(context: Context) = Room.databaseBuilder(context, RoomDB::class.java, "roomDB").allowMainThreadQueries().build()

class Repository private constructor(context: Context) : DataAccess by buildRoomDB(context)
{
    //TODO think about coroutines being cancelled because application is closed (sgould we create a background service?)

    suspend fun clearTable(tableUID: String)
    {
        tableDao().clearTable(tableUID)
        orderDao().clearTable(tableUID)
    }

    val networkRepository: NetworkRepositoryInterface = NetworkRepository(context)

    companion object
    {
        var instance: Repository? = null

        operator fun invoke(context: Context): Repository
        {
            synchronized(Repository::class.java) {
                return instance ?: Repository(context)
            }
        }
    }
}