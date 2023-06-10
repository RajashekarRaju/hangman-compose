package com.developersbreach.hangman.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Reusable composable for all 4 progress bars.
@Composable
fun CreateCircularProgressIndicator(
    currentProgress: Float,
    strokeWidth: Dp = 8.dp,
    progressColor: Color = MaterialTheme.colors.primary,
    indicatorSize: Dp
) {
    CircularProgressIndicator(
        progress = currentProgress,
        strokeWidth = strokeWidth,
        color = progressColor,
        modifier = Modifier.size(size = indicatorSize)
    )
}