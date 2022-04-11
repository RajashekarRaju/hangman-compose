package com.hangman.hangman.utils

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


@Composable
private fun applyTransitionState() = remember {
    MutableTransitionState(false).apply {
        // Start the animation immediately.
        targetState = true
    }
}

@Composable
fun ApplyAnimatedVisibility(
    content: @Composable () -> Unit,
    densityValue: Dp
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visibleState = applyTransitionState(),
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 500)
        ) {
            // Slide in from top/bottom the direction.
            with(density) { densityValue.roundToPx() }
        },
        content = { content() }
    )
}