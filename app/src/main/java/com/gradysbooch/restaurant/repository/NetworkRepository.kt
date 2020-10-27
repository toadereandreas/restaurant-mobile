package com.gradysbooch.restaurant.repository

import android.content.Context
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

open class NetworkRepository(val context: Context)
{
    @OptIn(ExperimentalCoroutinesApi::class)
    val internalOnlineStatus = MutableStateFlow(false)

    val onlineStatus: Flow<Boolean> = internalOnlineStatus
}