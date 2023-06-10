package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developersbreach.hangman.R
import com.developersbreach.hangman.ui.components.CreateCircularProgressIndicator

@Composable
fun LevelPointsAttemptsInformation(
    modifier: Modifier,
    currentPlayerLevel: Int,
    attemptsLeftToGuess: Int,
    pointsScoredOverall: Int,
    maxLevelReached: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        // Circular animated progress bars for attempts left and level streak.
        AttemptsLeftAndLevelProgressBars(
            currentPlayerLevel = currentPlayerLevel,
            attemptsLeftToGuess = attemptsLeftToGuess
        )

        // Text with points scored, levels completed.
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AttemptsLeftAndLevelText(
                pointsScoredOverall = pointsScoredOverall,
                currentPlayerLevel = currentPlayerLevel,
                maxLevelReached = maxLevelReached
            )
        }
    }
}

@Composable
private fun AttemptsLeftAndLevelProgressBars(
    currentPlayerLevel: Int,
    attemptsLeftToGuess: Int
) {
    // Animates and keeps track of current level player is in.
    CreateCircularProgressIndicator(
        currentProgress = animateCurrentLevelProgress(currentPlayerLevel),
        indicatorSize = 200.dp
    )

    // Doesn't animate level.
    // Filled with light primary color for player to understand the total levels to win.
    CreateCircularProgressIndicator(
        currentProgress = 1f,
        progressColor = MaterialTheme.colors.primary.copy(0.25f),
        indicatorSize = 200.dp
    )

    // Animates the current attempts completed in red color.
    // For each wrong guess, red progress will be filled.
    CreateCircularProgressIndicator(
        currentProgress = animateAttemptsLeftProgress(attemptsLeftToGuess),
        strokeWidth = 10.dp,
        indicatorSize = 240.dp,
        progressColor = Color.Red.copy(0.95f)
    )

    // Doesn't animate attempts.
    // Filled with green color for player to understand how many attempts he is left with.
    CreateCircularProgressIndicator(
        currentProgress = 1f,
        strokeWidth = 10.dp,
        progressColor = Color.Green.copy(0.25f),
        indicatorSize = 240.dp
    )
}

/**
 * These elements will be inside the circular progress bars.
 * Updates the current player game level to text.
 *
 */
@Composable
private fun AttemptsLeftAndLevelText(
    pointsScoredOverall: Int,
    currentPlayerLevel: Int,
    maxLevelReached: Int
) {
    // When player completes last level, the level value jumps to +1, to prevent that start level
    // from 0 and make sure to never increment level if max level reached.
    var incrementLevelBy = 0
    if (currentPlayerLevel < maxLevelReached) {
        incrementLevelBy = 1
    }

    Text(
        text = buildAnnotatedString {
            append(stringResource(id = R.string.current_level_header))
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colors.primary,
                    fontSize = 28.sp
                )
            ) {
                append("${currentPlayerLevel + incrementLevelBy}/$maxLevelReached")
            }
        },
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.primary.copy(0.50f),
        textAlign = TextAlign.Center
    )

    Divider(
        modifier = Modifier
            .width(width = 100.dp)
            .padding(vertical = 8.dp)
            .clip(MaterialTheme.shapes.small),
        color = MaterialTheme.colors.primary.copy(0.25f),
        thickness = 2.dp
    )

    Text(
        text = buildAnnotatedString {
            append(stringResource(id = R.string.current_points_header))
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colors.primary,
                    fontSize = 28.sp
                )
            ) {
                append(pointsScoredOverall.toString())
            }
        },
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.primary.copy(0.50f),
        textAlign = TextAlign.Center
    )
}