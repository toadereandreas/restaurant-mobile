package com.gradysbooch.restaurant.repository

import com.gradysbooch.restaurant.model.MenuItem
import com.gradysbooch.restaurant.model.Table
import kotlinx.coroutines.flow.Flow

interface NetworkRepositoryInterface
{
    val onlineStatus: Flow<Boolean>

    suspend fun getMenuItems(): Set<MenuItem>

    suspend fun getTables(): Set<Table>
}