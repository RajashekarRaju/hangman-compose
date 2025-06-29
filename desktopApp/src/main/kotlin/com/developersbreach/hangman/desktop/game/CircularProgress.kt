package com.developersbreach.hangman.desktop.game

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgress(
    progress: Float,
    strokeWidth: Dp = 8.dp,
    progressColor: Color = MaterialTheme.colors.primary,
    size: Dp
) {
    CircularProgressIndicator(
        progress = progress,
        strokeWidth = strokeWidth,
        color = progressColor,
        modifier = Modifier.size(size)
    )
}
