package com.gradysbooch.restaurant.model.dto

import com.gradysbooch.restaurant.model.Table

data class TableDTO(val id: String, val name: String, val call: Boolean)

fun Table.toDTO() = TableDTO(UID, name, call)
