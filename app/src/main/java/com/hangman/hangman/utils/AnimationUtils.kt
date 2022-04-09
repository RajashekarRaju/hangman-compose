package com.hangman.hangman.utils

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


@Composable
private fun applyTransitionState() = remember {
    MutableTransitionState(false).apply {
        // Start the animation immediately.
        targetState = true
    }
}

@Composable
fun ApplyAnimatedVisibility(
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visibleState = applyTransitionState(),
        enter = slideInVertically(
            animationSpec = tween(durationMillis = 500)
        ) {
            // Slide in from 400 dp from the top.
            with(density) { 400.dp.roundToPx() }
        },
        content = { content() }
    )
}