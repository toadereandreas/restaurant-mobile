package com.gradysbooch.restaurant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(@PrimaryKey val orderId: Int, val note: String)
