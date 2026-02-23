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
    return animateToTargetState(
        targetProgressValue = levelProgress(currentPlayerLevel)
    )
}

@Composable
fun animateAttemptsLeftProgress(
    attemptsLeft: Int
): Float {
    return animateToTargetState(
        targetProgressValue = attemptsUsedProgress(attemptsLeft)
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