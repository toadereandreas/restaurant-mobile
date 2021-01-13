package com.gradysbooch.restaurant.viewmodel

object ColorManager {
    private val colors = setOf<String>(
            "#40C8D4",
            "#A53860",
            "#560B27",
            "#A13FE8",
            "#3772FF",
            "#96031A",
            "#066E8B",
            "#7392B7",
            "#4F5D2F",
            "#F46036",
    )

    fun randomColor(currentColors: Set<String>): String? {
        val remainingColors = colors - currentColors
        if (remainingColors.isEmpty())
            return null

        return remainingColors.random()
    }
}