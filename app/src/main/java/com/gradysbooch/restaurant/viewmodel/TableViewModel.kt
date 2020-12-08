package com.gradysbooch.restaurant.viewmodel

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.gradysbooch.restaurant.model.dto.TableDTO
import com.gradysbooch.restaurant.model.dto.toDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TableViewModel(application: Application) : BaseViewModel(application),
    TableViewModelInterface {
    init
    {
        repository.networkRepository.getTables()
                .onEach { repository.tableDao().updateTables(it) }
                .launchIn(viewModelScope)
    }
    override val tables: Flow<List<TableDTO>> =
        repository.tableDao()
            .getTables()
            .map { tables ->
                tables.map { it.toDTO() }
            }
}