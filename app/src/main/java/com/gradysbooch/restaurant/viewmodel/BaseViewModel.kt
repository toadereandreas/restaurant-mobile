package com.gradysbooch.restaurant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.gradysbooch.restaurant.repository.Repository
import kotlinx.coroutines.flow.Flow

open class BaseViewModel(application: Application) : AndroidViewModel(application)
{
    protected val repository = Repository(application)

    val onlineStatus: Flow<Boolean> = repository.onlineStatus
}