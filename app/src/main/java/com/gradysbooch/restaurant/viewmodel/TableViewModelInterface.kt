package com.gradysbooch.restaurant.viewmodel

import com.gradysbooch.restaurant.model.Table
import kotlinx.coroutines.flow.Flow

interface TableViewModelInterface
{
    val tables: Flow<List<Table>>
}