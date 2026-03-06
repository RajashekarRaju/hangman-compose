package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.developersbreach.hangman.ui.game.simple.LevelPointsAttemptsVisual
import com.developersbreach.hangman.ui.game.traditional.TraditionalHangmanVisual

@Composable
internal fun GamePhaseProgress(
    uiState: GameUiState,
    progressScale: Float,
    modifier: Modifier,
) {
    when (uiState.progressVisualType) {
        GameProgressVisualType.LevelPointsAttemptsInformation -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier,
            ) {
                LevelPointsAttemptsVisual(
                    currentPlayerLevel = uiState.currentPlayerLevel,
                    attemptsLeftToGuess = uiState.attemptsLeftToGuess,
                    pointsScoredOverall = uiState.pointsScoredOverall,
                    maxLevelReached = uiState.maxLevelReached,
                    levelTimeProgress = uiState.levelTimeProgress,
                    progressScale = progressScale,
                    modifier = Modifier,
                )
            }
        }

        GameProgressVisualType.TraditionalHangman -> {
            val visualScale = progressScale.coerceIn(0.92f, 1.12f)
            val visualWidthFraction = (0.98f * visualScale).coerceIn(0.92f, 1f)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier,
            ) {
                TraditionalHangmanVisual(
                    attemptsLeftToGuess = uiState.attemptsLeftToGuess,
                    levelTimeProgress = uiState.levelTimeProgress,
                    modifier = Modifier.fillMaxWidth(visualWidthFraction),
                )
            }
        }
    }
}

