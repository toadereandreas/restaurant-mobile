package com.gradysbooch.restaurant.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["orderColor", "tableUID", "menuItemUID"])
@ForeignKey(entity = Order::class, parentColumns = ["orderColor", "tableUID"], childColumns = ["orderColor", "tableUID"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
data class OrderItem(

        @SerializedName("orderColor")
        val orderColor: String,

        @SerializedName("tableId")
        val tableUID: String,

        @SerializedName("menuItemId")
        @ColumnInfo(index = true)
        @ForeignKey(entity = MenuItem::class, parentColumns = ["menuItemUID"], childColumns = ["menuItemUID"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        val menuItemUID: String,

        @SerializedName("quantity")
        val quantity: Int
)