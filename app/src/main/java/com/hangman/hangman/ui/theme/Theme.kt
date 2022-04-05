package com.hangman.hangman.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val RedColorPalette = darkColors(
    primary = primary,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface
)

@Composable
fun RedHangmanTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = RedColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}