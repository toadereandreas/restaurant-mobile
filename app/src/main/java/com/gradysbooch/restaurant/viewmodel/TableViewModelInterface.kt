package com.gradysbooch.restaurant.viewmodel

import com.gradysbooch.restaurant.model.dto.TableDTO
import kotlinx.coroutines.flow.Flow

interface TableViewModelInterface
{
    val tables: Flow<List<TableDTO>>
}