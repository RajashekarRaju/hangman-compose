package com.developersbreach.hangman.ui.game.traditional

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.game.attemptsUsedProgress
import com.developersbreach.hangman.ui.game.timerFillProgress
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
internal fun TraditionalHangmanVisual(
    attemptsLeftToGuess: Int,
    levelTimeProgress: Float,
    playStageIntroAnimation: Boolean = true,
    modifier: Modifier = Modifier,
    style: TraditionalHangmanStyle? = null,
) {
    val resolvedStyle = style ?: themedWraithStyle()

    var stageIntroStarted by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        stageIntroStarted = true
    }

    val humanProgress by animateFloatAsState(
        targetValue = attemptsUsedProgress(attemptsLeftToGuess),
        animationSpec = tween(durationMillis = 420),
        label = "HumanProgress",
    )
    val timerProgress by animateFloatAsState(
        targetValue = timerFillProgress(levelTimeProgress),
        animationSpec = tween(durationMillis = 260),
        label = "TimerFillProgress",
    )
    val stageRevealProgress by animateFloatAsState(
        targetValue = when {
            playStageIntroAnimation -> if (stageIntroStarted) 1f else 0f
            else -> 1f
        },
        animationSpec = tween(durationMillis = 2_000, easing = FastOutSlowInEasing),
        label = "StageRevealProgress",
    )

    val infiniteTransition = rememberInfiniteTransition(label = "TraditionalHangmanMotion")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "TraditionalHangmanPhase",
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(292.dp),
    ) {
        drawWraithHangman(
            humanProgress = humanProgress,
            timerProgress = timerProgress,
            stageRevealProgress = stageRevealProgress,
            phase = phase,
            style = resolvedStyle,
        )
    }
}

@Composable
private fun themedWraithStyle(): TraditionalHangmanStyle {
    return TraditionalHangmanStyle(
        scaffoldColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.72f),
        bodyColor = HangmanTheme.colorScheme.onBackground.copy(alpha = 0.95f),
        outlineColor = HangmanTheme.colorScheme.background.copy(alpha = 0.96f),
        glowColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.54f),
        executionColor = HangmanTheme.colorScheme.error.copy(alpha = 0.90f),
        timerFillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.98f),
        timerHeadColor = HangmanTheme.colorScheme.tertiary.copy(alpha = 0.94f),
        lineWidthMultiplier = 1.34f,
        swingAmplitude = 4.6f,
        jitterAmplitude = 1.9f,
        creepiness = 0.18f,
    )
}
