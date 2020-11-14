package com.gradysbooch.restaurant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO add foreign key for table `Table`
@Entity
data class Order(@PrimaryKey val orderId: Int, val note: String)
