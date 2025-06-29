package com.developersbreach.hangman.desktop.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val tealColors = lightColors(
    primary = Color(0xFF9CF4FF),
    background = Color(0xFF1B2425),
    onBackground = Color(0xFFDDEBEC),
    surface = Color(0xFF212C2E),
    onSurface = Color(0xFFD2E5E7)
)

private val redColors = darkColors(
    primary = Color(0xFFFFA29C),
    background = Color(0xFF221918),
    onBackground = Color(0xFFE9DAD9),
    surface = Color(0xFF312423),
    onSurface = Color(0xFFECD9D8)
)

@Composable
fun DesktopHangmanTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    val colors = if (darkTheme) redColors else tealColors
    MaterialTheme(colors = colors, content = content)
}
