package com.developersbreach.hangman.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val TealColorPalette = lightColors(
    primary = tealPrimary,
    background = tealBackground,
    onBackground = tealOnBackground,
    surface = tealSurface,
    onSurface = tealOnSurface
)

private val RedColorPalette = darkColors(
    primary = redPrimary,
    background = redBackground,
    onBackground = redOnBackground,
    surface = redSurface,
    onSurface = redOnSurface
)

@Composable
fun RedHangmanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        RedColorPalette
    } else {
        TealColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}