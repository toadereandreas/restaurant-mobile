package com.gradysbooch.restaurant.model

import androidx.room.Embedded
import androidx.room.Relation

data class MenuItemWithOrderItem(
    @Embedded
    val menuItem: MenuItem,
    @Relation(
        parentColumn = "menuItemUID",
        entityColumn = "menuItemUID"
    )
    val orderItems: List<OrderItem>
)
