package com.gradysbooch.restaurant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Table(
        @PrimaryKey val id: Int,
        val name: String,
        val code: Int
)