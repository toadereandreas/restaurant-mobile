package com.gradysbooch.restaurant.model.dto

import com.gradysbooch.restaurant.model.MenuItem

data class MenuItemDTO(val id: String, val name: String, val price: Int)

fun MenuItem.toDTO() = MenuItemDTO(menuItemUID, name, price)