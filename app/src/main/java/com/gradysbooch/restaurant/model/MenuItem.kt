package com.gradysbooch.restaurant.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO Enable Full Text Search (FTS4) https://developer.android.com/training/data-storage/room/defining-data#fts
@Entity
data class MenuItem(
    @PrimaryKey val menuItemUID: String,
    val name: String,
    val price: Int
)