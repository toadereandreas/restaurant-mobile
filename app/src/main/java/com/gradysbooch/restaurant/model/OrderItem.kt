package com.gradysbooch.restaurant.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["orderId", "menuItemId"])
data class OrderItem(
        val orderId: Int,
        @ColumnInfo(index = true)
        val menuItemId: Int
)