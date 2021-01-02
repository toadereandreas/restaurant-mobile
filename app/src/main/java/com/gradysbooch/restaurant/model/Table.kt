package com.gradysbooch.restaurant.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Table(

        @SerializedName("tableId")
        @PrimaryKey
        val tableUID: String,

        @SerializedName("name")
        val name: String,

        @SerializedName("code")
        val code: Int?,

        @SerializedName("call")
        val call: Boolean
)