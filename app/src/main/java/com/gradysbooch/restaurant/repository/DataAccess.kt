package com.gradysbooch.restaurant.repository

interface DataAccess
{
    fun tableDao(): TableDAO
    fun orderDao(): OrderDAO
    fun menuItemDAO(): MenuItemDAO
}