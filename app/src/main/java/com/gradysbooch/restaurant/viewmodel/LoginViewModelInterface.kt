package com.gradysbooch.restaurant.viewmodel

interface LoginViewModelInterface
{
    suspend fun isLoggedIn(): Boolean

    suspend fun login(username: String, password: String): Boolean
}