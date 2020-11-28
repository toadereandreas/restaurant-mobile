package com.gradysbooch.restaurant.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class OrderWithMenuItems(
        @Embedded
        val order: Order,
        @Relation(
                parentColumn = "orderColor",
                entityColumn = "orderColor",
                entity = OrderItem::class
        )
        var orderItems: List<OrderItemWithMenuItem>
)