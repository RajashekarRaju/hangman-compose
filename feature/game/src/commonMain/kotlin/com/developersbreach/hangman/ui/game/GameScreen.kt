package com.developersbreach.hangman.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.developersbreach.game.core.alphabetsList
import com.developersbreach.hangman.ui.common.HangmanInstructionsDialog
import com.developersbreach.hangman.ui.preview.HangmanScreenPreviews
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GameScreen(
    navigateUp: () -> Unit,
    viewModel: GameViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                GameEffect.NavigateUp -> navigateUp()
            }
        }
    }

    PlatformBackHandler(enabled = true) {
        viewModel.onEvent(GameEvent.BackPressed)
    }

    if (uiState.revealGuessingWord) {
        ShowPopupWhenGameLost(
            wordToGuess = uiState.wordToGuess,
            onDismiss = { viewModel.onEvent(GameEvent.LostDialogDismissed) },
        )
    }

    if (uiState.gameOverByWinning) {
        ShowDialogWhenGameWon(
            pointsScoredOverall = uiState.pointsScoredOverall,
            gameDifficulty = uiState.gameDifficulty,
            onDismiss = { viewModel.onEvent(GameEvent.WinDialogDismissed) },
        )
    }

    if (uiState.showInstructionsDialog) {
        HangmanInstructionsDialog(
            difficultyValue = difficultyLabel(uiState.gameDifficulty),
            categoryValue = categoryLabel(uiState.gameCategory),
            onDismissRequest = { viewModel.onEvent(GameEvent.ToggleInstructionsDialog) },
        )
    }

    if (uiState.showExitDialog) {
        ShowExitGameDialog(
            onConfirmExit = { viewModel.onEvent(GameEvent.ExitConfirmed) },
            onDismiss = { viewModel.onEvent(GameEvent.ExitDismissed) },
        )
    }

    uiState.GameScreenUI(
        onEvent = viewModel::onEvent,
    )
}

@HangmanScreenPreviews
@Composable
private fun GameScreenPreviewContent() {
    HangmanTheme(
        darkTheme = true,
        palette = ThemePalettes.byId(ThemePaletteId.ORIGINAL),
    ) {
        GameUiState(
            currentPlayerLevel = 2,
            attemptsLeftToGuess = 5,
            pointsScoredOverall = 4,
            maxLevelReached = 5,
            alphabetsList = alphabetsList(),
            playerGuesses = listOf("H", "A", "N"),
        ).GameScreenUI {}
    }
}