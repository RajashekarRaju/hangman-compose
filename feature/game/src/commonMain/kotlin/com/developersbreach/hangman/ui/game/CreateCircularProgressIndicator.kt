package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun CreateCircularProgressIndicator(
    currentProgress: Float,
    strokeWidth: Dp = 8.dp,
    progressColor: Color = HangmanTheme.colorScheme.primary,
    indicatorSize: Dp,
) {
    CircularProgressIndicator(
        progress = { currentProgress },
        strokeWidth = strokeWidth,
        color = progressColor,
        trackColor = Color.Transparent,
        modifier = Modifier.size(size = indicatorSize),
    )
}