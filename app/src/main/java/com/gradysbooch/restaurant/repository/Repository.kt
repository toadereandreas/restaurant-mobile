package com.gradysbooch.restaurant.repository

import android.content.Context
import com.gradysbooch.restaurant.model.MenuItem
import com.gradysbooch.restaurant.model.Table

class Repository private constructor(context: Context)
{
    private val networkRepository = NetworkRepository(context)

    val onlineStatus get() = networkRepository.onlineStatus

    suspend fun getMenuItems () : Set<MenuItem> {
        return networkRepository.getMenuItems();
    }

    suspend fun getTables () : Set<Table> {
        return networkRepository.getTables();
    }

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