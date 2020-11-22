package com.gradysbooch.restaurant.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gradysbooch.restaurant.model.*

@Database(
    entities = [MenuItem::class, Order::class, OrderItem::class, Table::class],
    version = 3,
    exportSchema = false
)
abstract class RoomDB : RoomDatabase(), DataAccess {
    abstract override fun tableDao(): TableDAO
    abstract override fun orderDao(): OrderDAO
    abstract override fun menuItemDAO(): MenuItemDAO
}