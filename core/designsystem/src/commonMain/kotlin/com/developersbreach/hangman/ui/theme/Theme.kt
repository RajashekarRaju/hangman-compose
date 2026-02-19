package com.developersbreach.hangman.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun HangmanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    palette: ThemePalette = ThemePalettes.default,
    content: @Composable () -> Unit,
) {
    val colorScheme = remember(darkTheme, palette) {
        palette.toColorScheme(darkTheme)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(),
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                content()
            }
        },
    )
}
