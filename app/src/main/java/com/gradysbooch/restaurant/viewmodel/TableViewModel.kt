package com.gradysbooch.restaurant.viewmodel

import android.app.Application
import com.gradysbooch.restaurant.model.dto.TableDTO
import kotlinx.coroutines.flow.Flow

class TableViewModel(application: Application) : BaseViewModel(application), TableViewModelInterface
{
    override val tables: Flow<List<TableDTO>> = TODO()
}