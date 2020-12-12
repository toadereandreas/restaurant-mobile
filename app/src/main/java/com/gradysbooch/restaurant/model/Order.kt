package com.gradysbooch.restaurant.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["orderColor", "tableUID"])
data class Order(

    @SerializedName("tableId")
    @ForeignKey(
        entity = Table::class,
        parentColumns = ["UID"],
        childColumns = ["tableUID"],
        onDelete = CASCADE,
        onUpdate = CASCADE
    )
    val tableUID: String,

    @SerializedName("orderColor")
    val orderColor: String,

    @SerializedName("note")
    val note: String
)
