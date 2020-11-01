package com.gradysbooch.restaurant.viewmodel

import android.app.Application

class LoginViewModel(application: Application) : BaseViewModel(application), LoginViewModelInterface
{
    override suspend fun isLoggedIn(): Boolean
    {
        TODO()
    }

    override suspend fun login(username: String, password:String): Boolean
    {
        TODO("Decide on login method")
    }
}