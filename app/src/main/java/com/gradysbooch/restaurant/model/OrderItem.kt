package com.gradysbooch.restaurant.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(primaryKeys = ["orderColor", "tableUID", "menuItemUID"])
@ForeignKey(entity = Order::class, parentColumns = ["orderColor", "tableUID"], childColumns = ["orderColor", "tableUID"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
data class OrderItem(
        val orderColor: String,
        val tableUID: String,
        @ColumnInfo(index = true)
        @ForeignKey(entity = MenuItem::class, parentColumns = ["menuItemUID"], childColumns = ["menuItemUID"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        val menuItemUID: String,
        val quantity: Int
)