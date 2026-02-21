package com.developersbreach.hangman.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
private fun rememberVisibleTransitionState(): MutableTransitionState<Boolean> {
    return remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
}

@Composable
fun AnimatedEnter(
    modifier: Modifier = Modifier,
    offsetY: Dp = 32.dp,
    durationMillis: Int = 450,
    content: @Composable () -> Unit,
) {
    val density = LocalDensity.current
    AnimatedVisibility(
        visibleState = rememberVisibleTransitionState(),
        enter = fadeIn(animationSpec = tween(durationMillis = durationMillis)) +
            slideInVertically(
                animationSpec = tween(durationMillis = durationMillis),
                initialOffsetY = { with(density) { offsetY.roundToPx() } },
            ),
        modifier = modifier,
        content = { content() },
    )
}

@Composable
fun rememberInfiniteRotation(
    initialValue: Float = 0f,
    targetValue: Float = 360f,
    repeatMode: RepeatMode = RepeatMode.Restart,
    durationMillis: Int = 5000,
    delayMillis: Int = 0,
    easing: Easing = LinearEasing,
): Float {
    val transition = rememberInfiniteTransition(label = "InfiniteRotationTransition")
    val rotation by transition.animateFloat(
        label = "InfiniteRotation",
        initialValue = initialValue,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis,
                easing = easing,
            ),
            repeatMode = repeatMode,
        ),
    )
    return rotation
}

@Composable
fun rememberCreepyPhase(
    durationMillis: Int = 3200,
): Float {
    val transition = rememberInfiniteTransition(label = "CreepyPhaseTransition")
    val phase by transition.animateFloat(
        label = "CreepyPhase",
        initialValue = 0f,
        targetValue = (2f * kotlin.math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
    )
    return phase
}

@Composable
fun SparkPulse(
    modifier: Modifier = Modifier,
    sparkColor: Color = HangmanTheme.colorScheme.primary.copy(alpha = 0.50f),
) {
    val transition = rememberInfiniteTransition(label = "SparkPulseTransition")
    val scale by transition.animateFloat(
        label = "SparkPulseScale",
        initialValue = 0f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000),
            repeatMode = RepeatMode.Restart,
        ),
    )

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
    ) {
        drawCircle(
            color = sparkColor,
            center = Offset(x = size.width / 2, y = size.height / 2),
            radius = size.minDimension / 4,
            style = Stroke(
                width = 12f,
                pathEffect = PathEffect.dashPathEffect(intervals = floatArrayOf(6f, 12f)),
            ),
        )
    }
}
