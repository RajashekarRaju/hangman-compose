package com.developersbreach.hangman.ui.game.visual

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.repository.GameProgressVisualPreference
import com.developersbreach.hangman.ui.game.simple.LevelPointsAttemptsVisual
import com.developersbreach.hangman.ui.game.traditional.TraditionalHangmanVisual

@Composable
fun GameProgressVisualPreview(
    gameProgressVisualPreference: GameProgressVisualPreference,
    modifier: Modifier = Modifier,
) {
    when (gameProgressVisualPreference) {
        GameProgressVisualPreference.LEVEL_POINTS_ATTEMPTS -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.height(220.dp),
            ) {
                LevelPointsAttemptsVisual(
                    modifier = Modifier.fillMaxWidth(0.72f),
                    currentPlayerLevel = 2,
                    attemptsLeftToGuess = 5,
                    pointsScoredOverall = 28,
                    maxLevelReached = 5,
                    levelTimeProgress = 0.65f,
                    progressScale = 0.84f,
                )
            }
        }

        GameProgressVisualPreference.TRADITIONAL_HANGMAN -> {
            TraditionalHangmanVisual(
                attemptsLeftToGuess = 2,
                levelTimeProgress = 0.70f,
                playStageIntroAnimation = false,
                modifier = modifier
                    .fillMaxWidth()
                    .height(220.dp),
            )
        }
    }
}
