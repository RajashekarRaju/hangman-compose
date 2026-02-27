package com.developersbreach.hangman.ui.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.model.HistoryRecord
import com.developersbreach.hangman.ui.preview.HangmanScreenPreviews
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HistoryScreen(
    navigateUp: () -> Unit,
    viewModel: HistoryViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                HistoryEffect.NavigateUp -> navigateUp()
            }
        }
    }

    uiState.HistoryScreenUI(onEvent = viewModel::onEvent)
}

@HangmanScreenPreviews
@Composable
private fun HistoryScreenUIPreview() {
    val record = HistoryRecord(
        gameId = "1",
        gameScore = 2913,
        gameLevel = 4,
        gameDifficulty = GameDifficulty.HARD,
        gameCategory = GameCategory.LANGUAGES,
        gameSummary = false,
        gamePlayedTime = "09:41 PM",
        gamePlayedDate = "14 Feb"
    )
    HangmanTheme(
        darkTheme = true,
        palette = ThemePalettes.byId(ThemePaletteId.ORIGINAL)
    ) {
        HistoryUiState(
            gameHistoryList = listOf(
                HistoryListItemUiState(
                    history = record,
                    levelProgress = 0.8f,
                    hintTypeLabelRes = emptyList(),
                ),
                HistoryListItemUiState(
                    history = record.copy(gameId = "2"),
                    levelProgress = 0.8f,
                    hintTypeLabelRes = emptyList(),
                ),
                HistoryListItemUiState(
                    history = record.copy(gameId = "3"),
                    levelProgress = 0.8f,
                    hintTypeLabelRes = emptyList(),
                ),
            )
        ).HistoryScreenUI {}
    }
}
