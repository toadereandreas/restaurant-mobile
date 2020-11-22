package com.gradysbooch.restaurant.model.dto

typealias Color = String

data class AllScreenItem(
    val menuItem: MenuItemDTO,
    val number: Int,
    val orders: List<Pair<Color, Int>>
)