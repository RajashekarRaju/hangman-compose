package com.developersbreach.hangman.ui.game

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*


@Composable
fun animateCurrentLevelProgress(
    currentPlayerLevel: Int
): Float {
    var currentLevelProgress by remember { mutableStateOf(0f) }

    when (currentPlayerLevel) {
        0 -> currentLevelProgress = 0f
        1 -> currentLevelProgress = 0.2f
        2 -> currentLevelProgress = 0.4f
        3 -> currentLevelProgress = 0.6f
        4 -> currentLevelProgress = 0.8f
        5 -> currentLevelProgress = 1f
    }

    return animateToTargetState(
        targetProgressValue = currentLevelProgress
    )
}

@Composable
fun animateAttemptsLeftProgress(
    attemptsLeft: Int
): Float {
    var currentAttemptsLeftProgress by remember { mutableStateOf(0f) }

    when (attemptsLeft) {
        8 -> currentAttemptsLeftProgress = 0f
        7 -> currentAttemptsLeftProgress = 0.13f
        6 -> currentAttemptsLeftProgress = 0.25f
        5 -> currentAttemptsLeftProgress = 0.37f
        4 -> currentAttemptsLeftProgress = 0.50f
        3 -> currentAttemptsLeftProgress = 0.63f
        2 -> currentAttemptsLeftProgress = 0.75f
        1 -> currentAttemptsLeftProgress = 0.87f
        0 -> currentAttemptsLeftProgress = 1f
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
        targetValue = targetProgressValue,
        animationSpec = tween(
            durationMillis = durationMillis,
            delayMillis = delayMillis,
            easing = easing
        )
    )
    return animatedProgress
}