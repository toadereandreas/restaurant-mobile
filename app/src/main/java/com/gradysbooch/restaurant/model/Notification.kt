package com.gradysbooch.restaurant.model

import java.time.LocalDate

class Notification(
    val id: Int,
    val description: String,
    val date: LocalDate,
    val userId: Int) {
}