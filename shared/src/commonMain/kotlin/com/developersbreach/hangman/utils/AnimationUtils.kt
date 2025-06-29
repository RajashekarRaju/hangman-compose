package com.developersbreach.hangman.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

// Manages the state for animations state.
@Composable
private fun applyTransitionState() = remember {
    MutableTransitionState(false).apply {
        // Start the animation immediately.
        targetState = true
    }
}

// Animation to slide any element vertically from it's density.
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

/**
 * Infinitely repeatable animation which rotates any element on applied.
 */
@Composable
fun createInfiniteRepeatableRotateAnimation(
    initialValue: Float = 0f,
    targetValue: Float = 360f,
    repeatMode: RepeatMode = RepeatMode.Restart,
    durationMillis: Int = 5000,
    delayMillis: Int = 0,
    easing: Easing = LinearEasing
): Float {
    val infiniteRotateAnim = rememberInfiniteTransition(label = "RotateInfiniteTransition")

    val rotateAnimation by infiniteRotateAnim.animateFloat(
        label = "RotateAnimation",
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            repeatMode = repeatMode,
            animation = tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = easing
            )
        )
    )

    return rotateAnimation
}

/**
 * Infinitely draws circles to create path of sparks on the canvas.
 * Needs rework, iterate once only.
 */
@Composable
fun SparkAnimateGuessedLetter(
    sparkColor: Color = MaterialTheme.colors.primary.copy(0.50f)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SparkInfiniteTransition")
    val scale: Float by infiniteTransition.animateFloat(
        label = "SparkAnimation",
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawCircle(
            color = sparkColor,
            center = Offset(
                x = canvasWidth / 2,
                y = canvasHeight / 2
            ),
            radius = size.minDimension / 4,
            style = Stroke(
                width = 12f,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(6f, 12f)
                )
            )
        )
    }
}