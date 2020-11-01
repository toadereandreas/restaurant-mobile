package com.gradysbooch.restaurant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gradysbooch.restaurant.model.Table
import com.gradysbooch.restaurant.repository.Repository
import kotlinx.coroutines.flow.Flow

class TableViewModel(application: Application) : BaseViewModel(application), TableViewModelInterface
{
    override val tables: Flow<List<Table>> = TODO()
}