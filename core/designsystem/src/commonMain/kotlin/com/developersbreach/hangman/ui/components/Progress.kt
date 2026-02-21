package com.developersbreach.hangman.ui.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun HangmanCircularProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 2.dp,
    indicatorColor: Color,
    trackColor: Color = Color.Transparent,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
) {
    CircularProgressIndicator(
        progress = { progress },
        modifier = modifier,
        color = indicatorColor,
        trackColor = trackColor,
        strokeWidth = strokeWidth,
        strokeCap = strokeCap,
    )
}
