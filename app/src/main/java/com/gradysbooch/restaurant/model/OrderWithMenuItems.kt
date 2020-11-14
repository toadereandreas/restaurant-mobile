package com.gradysbooch.restaurant.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class OrderWithMenuItems(
    @Embedded
    val order: Order,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "menuItemId",
        associateBy = Junction(OrderItem::class)
    )
    var menuItems: List<MenuItem>
)