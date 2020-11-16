package com.gradysbooch.restaurant.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class Table(
        @PrimaryKey
        val UID: String,
        val name: String,
        val code: Int,
        val call: Boolean
)