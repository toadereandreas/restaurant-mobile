package com.gradysbooch.restaurant.viewmodel

import android.app.Application
import com.gradysbooch.restaurant.model.dto.TableDTO
import com.gradysbooch.restaurant.model.dto.toDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TableViewModel(application: Application) : BaseViewModel(application),
    TableViewModelInterface {
    override val tables: Flow<List<TableDTO>> =
        repository.tableDao()
            .getTables()
            .map { tables ->
                tables.map { it.toDTO() }
            }
}