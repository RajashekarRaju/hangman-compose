package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.components.HangmanCircularProgress
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun CreateCircularProgressIndicator(
    currentProgress: Float,
    strokeWidth: Dp = 8.dp,
    progressColor: Color = HangmanTheme.colorScheme.primary,
    indicatorSize: Dp,
) {
    HangmanCircularProgress(
        progress = currentProgress,
        modifier = Modifier.size(indicatorSize),
        strokeWidth = strokeWidth,
        indicatorColor = progressColor,
        trackColor = Color.Transparent,
    )
}