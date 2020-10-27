package com.gradysbooch.restaurant.repository

import android.content.Context

class Repository private constructor(context: Context)
{
    private val networkRepository = NetworkRepository(context)
    val onlineStatus get() = networkRepository.onlineStatus

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