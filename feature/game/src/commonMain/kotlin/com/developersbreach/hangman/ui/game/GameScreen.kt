package com.developersbreach.hangman.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
        GameInstructionsInfoDialog(
            gameDifficulty = uiState.gameDifficulty,
            gameCategory = uiState.gameCategory,
            onDismiss = { viewModel.onEvent(GameEvent.ToggleInstructionsDialog) },
        )
    }

    if (uiState.showExitDialog) {
        ShowExitGameDialog(
            onConfirmExit = { viewModel.onEvent(GameEvent.ExitConfirmed) },
            onDismiss = { viewModel.onEvent(GameEvent.ExitDismissed) },
        )
    }

    GameScreenUI(
        uiState = uiState,
        onEvent = viewModel::onEvent,
    )
}
