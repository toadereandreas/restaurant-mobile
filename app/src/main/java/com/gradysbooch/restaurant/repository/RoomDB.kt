package com.gradysbooch.restaurant.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gradysbooch.restaurant.model.*

@Database(
    entities = [MenuItem::class, Order::class, OrderItem::class, Table::class],
    version = 2,
    exportSchema = false
)
abstract class RoomDB : RoomDatabase() {
    abstract fun dao(): DAO
}