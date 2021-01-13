package com.gradysbooch.restaurant.viewmodel

import androidx.compose.ui.graphics.Color
import java.lang.RuntimeException

object ColorManager {
    private val colorMap = mapOf<String, Color>(
        "red" to Color.Red,
        "green" to Color.Green,
        "blue" to Color.Blue,
        "yellow" to Color.Yellow,
        "cyan" to Color.Cyan,
        "magenta" to Color.Magenta,
    )

    fun convertToColor(color: String): Color = colorMap[color] ?: error("Could not convert color")

    fun randomColor(currentColors: Set<String>): String {
        val remainingColors = colorMap.keys - currentColors
        if (remainingColors.isEmpty())
            throw RuntimeException("We ran out of colors")

        return remainingColors.random()
    }
}