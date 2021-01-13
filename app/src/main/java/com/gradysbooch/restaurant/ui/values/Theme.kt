package com.gradysbooch.restaurant.ui.values

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

/*
Dark:
    Background: lightBlack
    Surface: lightBlack
    Border: darkWhite

Light:
    Background: white
    Surface: white
    Border: lightBlack
 */


private val DarkColorPalette = darkColors(
        primary = lightBlack,
        primaryVariant = black,
        background = lightBlack,
        surface = lightBlack,
        error = red,
        onPrimary = black,
        onBackground = darkWhite,
        onSurface = darkWhite,
        onError = darkWhite,

        secondary = darkWhite,
        onSecondary = lightBlack
    /* OLD
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
     */
)

private val LightColorPalette = lightColors(
        primary = white,
        primaryVariant = darkWhite,
        background = white,
        surface = white,
        error = red,
        onPrimary = black,
        onBackground = black,
        onSurface = black,
        onError = black,

        secondary = lightBlack,
        onSecondary = darkWhite
    /* OLD
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