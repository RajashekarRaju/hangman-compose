package com.developersbreach.hangman.ui.game

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

@Composable
fun animateCurrentLevelProgress(
    currentPlayerLevel: Int
): Float {
    val currentLevelProgress = when (currentPlayerLevel.coerceIn(0, 5)) {
        0 -> 0f
        1 -> 0.2f
        2 -> 0.4f
        3 -> 0.6f
        4 -> 0.8f
        5 -> 1f
        else -> 0f
    }

    return animateToTargetState(
        targetProgressValue = currentLevelProgress
    )
}

@Composable
fun animateAttemptsLeftProgress(
    attemptsLeft: Int
): Float {
    val currentAttemptsLeftProgress = when (attemptsLeft.coerceIn(0, 8)) {
        8 -> 0f
        7 -> 0.13f
        6 -> 0.25f
        5 -> 0.37f
        4 -> 0.50f
        3 -> 0.63f
        2 -> 0.75f
        1 -> 0.87f
        0 -> 1f
        else -> 0f
    }

    return animateToTargetState(
        targetProgressValue = currentAttemptsLeftProgress
    )
}

@Composable
private fun animateToTargetState(
    targetProgressValue: Float,
    durationMillis: Int = 500,
    delayMillis: Int = 0,
    easing: Easing = LinearEasing
): Float {
    val animatedProgress by animateFloatAsState(
        label = "AttemptsLeftAnimation",
        targetValue = targetProgressValue,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing
        )
    )
    return animatedProgress
}