package com.gradysbooch.restaurant.model.dto

import com.gradysbooch.restaurant.model.MenuItem

data class MenuItemDTO(val id: Int, val name: String, val price: Int)

fun MenuItem.toDTO() = MenuItemDTO(menuItemId, name, price)