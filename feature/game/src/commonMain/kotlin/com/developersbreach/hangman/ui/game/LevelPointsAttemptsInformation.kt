package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_current_level
import com.developersbreach.hangman.feature.game.generated.resources.game_current_points
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.HeadlineSmallText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun LevelPointsAttemptsInformation(
    modifier: Modifier,
    currentPlayerLevel: Int,
    attemptsLeftToGuess: Int,
    pointsScoredOverall: Int,
    maxLevelReached: Int,
) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        AttemptsLeftAndLevelProgressBars(
            currentPlayerLevel = currentPlayerLevel,
            attemptsLeftToGuess = attemptsLeftToGuess,
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AttemptsLeftAndLevelText(
                pointsScoredOverall = pointsScoredOverall,
                currentPlayerLevel = currentPlayerLevel,
                maxLevelReached = maxLevelReached,
            )
        }
    }
}

@Composable
private fun AttemptsLeftAndLevelProgressBars(
    currentPlayerLevel: Int,
    attemptsLeftToGuess: Int,
) {
    CreateCircularProgressIndicator(
        currentProgress = animateCurrentLevelProgress(currentPlayerLevel),
        indicatorSize = 200.dp,
    )

    CreateCircularProgressIndicator(
        currentProgress = 1f,
        progressColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.25f),
        indicatorSize = 200.dp,
    )

    CreateCircularProgressIndicator(
        currentProgress = animateAttemptsLeftProgress(attemptsLeftToGuess),
        strokeWidth = 10.dp,
        indicatorSize = 240.dp,
        progressColor = HangmanTheme.colorScheme.error.copy(alpha = 0.95f),
    )

    CreateCircularProgressIndicator(
        currentProgress = 1f,
        strokeWidth = 10.dp,
        progressColor = HangmanTheme.colorScheme.tertiary.copy(alpha = 0.25f),
        indicatorSize = 240.dp,
    )
}

@Composable
private fun AttemptsLeftAndLevelText(
    pointsScoredOverall: Int,
    currentPlayerLevel: Int,
    maxLevelReached: Int,
) {
    val displayedLevel = if (currentPlayerLevel < maxLevelReached) {
        currentPlayerLevel + 1
    } else {
        maxLevelReached
    }

    BodyLargeText(
        text = stringResource(Res.string.game_current_level, displayedLevel, maxLevelReached),
        color = HangmanTheme.colorScheme.primary.copy(alpha = 0.50f),
        textAlign = TextAlign.Center,
    )

    HorizontalDivider(
        modifier = Modifier
            .width(width = 100.dp)
            .padding(vertical = 8.dp)
            .clip(HangmanTheme.shapes.small),
        color = HangmanTheme.colorScheme.primary.copy(alpha = 0.25f),
        thickness = 2.dp,
    )

    HeadlineSmallText(
        text = stringResource(Res.string.game_current_points, pointsScoredOverall),
        color = HangmanTheme.colorScheme.primary,
        textAlign = TextAlign.Center,
    )
}