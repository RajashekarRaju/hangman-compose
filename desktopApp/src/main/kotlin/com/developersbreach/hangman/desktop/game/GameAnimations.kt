package com.developersbreach.hangman.desktop.game

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun animateCurrentLevelProgress(currentPlayerLevel: Int): Float {
    var currentLevelProgress by remember { mutableFloatStateOf(0f) }
    currentLevelProgress = when (currentPlayerLevel) {
        0 -> 0f
        1 -> 0.2f
        2 -> 0.4f
        3 -> 0.6f
        4 -> 0.8f
        else -> 1f
    }
    return animateToTarget(currentLevelProgress)
}

@Composable
fun animateAttemptsLeftProgress(attemptsLeft: Int): Float {
    var progress by remember { mutableFloatStateOf(0f) }
    progress = when (attemptsLeft) {
        8 -> 0f
        7 -> 0.13f
        6 -> 0.25f
        5 -> 0.37f
        4 -> 0.50f
        3 -> 0.63f
        2 -> 0.75f
        1 -> 0.87f
        else -> 1f
    }
    return animateToTarget(progress)
}

@Composable
private fun animateToTarget(
    target: Float,
    durationMillis: Int = 500,
    delayMillis: Int = 0,
    easing: Easing = LinearEasing
): Float {
    val animated by animateFloatAsState(
        targetValue = target,
        animationSpec = tween(durationMillis, delayMillis, easing),
        label = "progress"
    )
    return animated
}
