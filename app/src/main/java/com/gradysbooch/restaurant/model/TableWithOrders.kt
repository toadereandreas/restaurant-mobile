package com.gradysbooch.restaurant.model

import androidx.room.Embedded
import androidx.room.Relation

data class TableWithOrders(
       @Embedded val table: Table,
       @Relation(
               parentColumn = "UID",
               entityColumn = "tableUID"
       )
       val orders: List<Order>
)
