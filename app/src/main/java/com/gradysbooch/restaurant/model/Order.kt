package com.gradysbooch.restaurant.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//TODO add foreign key for table `Table`
@Entity
data class Order(
    @PrimaryKey
        val orderId: Int,
    @ForeignKey(entity = Table::class, childColumns = arrayOf("tableId"), parentColumns = arrayOf("id")) //todo Andu a pus foreign key aici, pray it works :))
        val tableId: Int,
    val note: String
    )
