package com.gradysbooch.restaurant.model.dto

data class Bullet(val color: String, val locked: Boolean, val pressed: Boolean) {
    override fun equals(other: Any?): Boolean
    {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bullet

        if (color != other.color) return false

        return true
    }

    override fun hashCode(): Int
    {
        return color.hashCode()
    }
}
