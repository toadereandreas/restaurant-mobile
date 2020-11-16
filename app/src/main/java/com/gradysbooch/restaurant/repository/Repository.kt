package com.gradysbooch.restaurant.repository

import android.content.Context
import androidx.room.Room

fun buildRoomDB(context: Context) = Room.databaseBuilder(context, RoomDB::class.java, "roomDB").build()

class Repository private constructor(context: Context) : DataAccess by buildRoomDB(context)
{
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