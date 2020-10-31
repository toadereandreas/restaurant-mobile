package com.gradysbooch.restaurant.ui.values

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = lightBlack,
    primaryVariant = black,
    background = lightBlack,
    surface = darkGray,
    error = red,
    onPrimary = darkWhite,
    onBackground = darkWhite,
    onSurface = darkWhite,
    onError = darkWhite,

    secondary = darkWhite,
    onSecondary = black
    /*
    Default Android Studio values
        primary = purple200,
        primaryVariant = purple700,
        secondary = teal200
     */
)

private val LightColorPalette = lightColors(
    primary = darkWhite,
    primaryVariant = white,
    background = darkWhite,
    surface = lightGray,
    error = red,
    onPrimary = black,
    onBackground = black,
    onSurface = black,
    onError = black,

    secondary = lightBlack,
    secondaryVariant = black,
    onSecondary = white
    /*
    Default Android Studio values
        primary = purple500,
        primaryVariant = purple700,
        secondary = teal200
     */
)

@Composable
fun RestaurantmobileTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    MaterialTheme(
            colors = if (darkTheme) DarkColorPalette else LightColorPalette,
            typography = typography,
            shapes = shapes,
            content = content
    )
}