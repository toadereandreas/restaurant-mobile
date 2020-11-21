package com.gradysbooch.restaurant.model

import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.Relation

data class OrderItemWithMenuItem(
        @Embedded
        val orderItem: OrderItem,
        @Relation(
                parentColumn = "orderColor",
                entityColumn = "menuItemUID"
        )
        val menuItem: MenuItem
)