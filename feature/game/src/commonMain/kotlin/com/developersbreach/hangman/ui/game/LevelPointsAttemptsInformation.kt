package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.core.designsystem.generated.resources.Res as DesignRes
import com.developersbreach.hangman.core.designsystem.generated.resources.game_background
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_current_level
import com.developersbreach.hangman.feature.game.generated.resources.game_current_points
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.HangmanDivider
import com.developersbreach.hangman.ui.components.HeadlineSmallText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LevelPointsAttemptsInformation(
    modifier: Modifier,
    currentPlayerLevel: Int,
    attemptsLeftToGuess: Int,
    pointsScoredOverall: Int,
    maxLevelReached: Int,
    levelTimeProgress: Float,
    progressScale: Float = 1f,
) {
    val normalizedScale = progressScale.coerceIn(0.84f, 1.12f)
    val innerIndicatorSize = 200.dp * normalizedScale
    val outerIndicatorSize = 240.dp * normalizedScale
    val outerStrokeWidth = 10.dp * normalizedScale
    val ringSafetyPadding = outerStrokeWidth * 0.95f
    val dividerWidth = 100.dp * normalizedScale
    val dividerVerticalPadding = 8.dp * normalizedScale
    val dividerThickness = 2.dp * normalizedScale

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.padding(ringSafetyPadding),
    ) {
        TimerDecayBackground(
            timeProgress = levelTimeProgress,
            indicatorSize = outerIndicatorSize,
        )

        AttemptsLeftAndLevelProgressBars(
            currentPlayerLevel = currentPlayerLevel,
            attemptsLeftToGuess = attemptsLeftToGuess,
            innerIndicatorSize = innerIndicatorSize,
            outerIndicatorSize = outerIndicatorSize,
            outerStrokeWidth = outerStrokeWidth,
        )

        TextReadabilityBackdrop(
            indicatorSize = innerIndicatorSize * 0.5f,
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AttemptsLeftAndLevelText(
                pointsScoredOverall = pointsScoredOverall,
                currentPlayerLevel = currentPlayerLevel,
                maxLevelReached = maxLevelReached,
                dividerWidth = dividerWidth,
                dividerVerticalPadding = dividerVerticalPadding,
                dividerThickness = dividerThickness,
            )
        }
    }
}

@Composable
private fun TimerDecayBackground(
    timeProgress: Float,
    indicatorSize: Dp,
) {
    val targetProgress = timeProgress.coerceIn(0f, 1f)
    val animatedVisibleProgress = remember { Animatable(targetProgress) }
    LaunchedEffect(targetProgress) {
        animatedVisibleProgress.animateTo(
            targetValue = targetProgress,
            animationSpec = tween(durationMillis = 420, easing = LinearEasing),
        )
    }
    Box(
        modifier = Modifier
            .size(indicatorSize)
            .clip(CircleShape),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Image(
            painter = painterResource(DesignRes.drawable.game_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alpha = 0.15f,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    val top = size.height * (1f - animatedVisibleProgress.value)
                    clipRect(top = top, bottom = size.height) {
                        this@drawWithContent.drawContent()
                    }
                },
        )
    }
}

@Composable
private fun TextReadabilityBackdrop(
    indicatorSize: Dp,
) {
    Box(
        modifier = Modifier
            .size(indicatorSize)
            .clip(CircleShape)
            .background(HangmanTheme.colorScheme.surface.copy(alpha = 0.25f)),
    )
}

@Composable
private fun AttemptsLeftAndLevelProgressBars(
    currentPlayerLevel: Int,
    attemptsLeftToGuess: Int,
    innerIndicatorSize: Dp,
    outerIndicatorSize: Dp,
    outerStrokeWidth: Dp,
) {
    Box(
        modifier = Modifier.size(outerIndicatorSize),
        contentAlignment = Alignment.Center,
    ) {
        CreateCircularProgressIndicator(
            currentProgress = animateCurrentLevelProgress(currentPlayerLevel),
            indicatorSize = innerIndicatorSize,
        )

        CreateCircularProgressIndicator(
            currentProgress = 1f,
            progressColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.25f),
            indicatorSize = innerIndicatorSize,
        )

        CreateCircularProgressIndicator(
            currentProgress = animateAttemptsLeftProgress(attemptsLeftToGuess),
            strokeWidth = outerStrokeWidth,
            indicatorSize = outerIndicatorSize,
            progressColor = HangmanTheme.colorScheme.error.copy(alpha = 0.95f),
        )

        CreateCircularProgressIndicator(
            currentProgress = 1f,
            strokeWidth = outerStrokeWidth,
            progressColor = HangmanTheme.colorScheme.tertiary.copy(alpha = 0.25f),
            indicatorSize = outerIndicatorSize,
        )
    }
}

@Composable
private fun AttemptsLeftAndLevelText(
    pointsScoredOverall: Int,
    currentPlayerLevel: Int,
    maxLevelReached: Int,
    dividerWidth: Dp,
    dividerVerticalPadding: Dp,
    dividerThickness: Dp,
) {
    val displayedLevel = resolveDisplayedLevel(currentPlayerLevel, maxLevelReached)
    BodyLargeText(
        text = stringResource(Res.string.game_current_level, displayedLevel, maxLevelReached),
        color = HangmanTheme.colorScheme.primary.copy(alpha = 0.50f),
        textAlign = TextAlign.Center,
    )

    HangmanDivider(
        modifier = Modifier
            .width(width = dividerWidth)
            .padding(vertical = dividerVerticalPadding)
            .clip(HangmanTheme.shapes.small),
        seed = 941,
        threshold = 0.06f,
        outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.28f),
        color = HangmanTheme.colorScheme.primary.copy(alpha = 0.25f),
        thickness = dividerThickness,
    )

    HeadlineSmallText(
        text = stringResource(Res.string.game_current_points, pointsScoredOverall),
        color = HangmanTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
    )
}