package com.gradysbooch.restaurant.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["orderColor", "tableUID"])
data class Order(
    @ForeignKey(
        entity = Table::class,
        parentColumns = ["UID"],
        childColumns = ["tableUID"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )
    val tableUID: String,
    val orderColor: String,
    val note: String
)
