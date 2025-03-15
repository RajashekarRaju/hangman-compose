package com.developersbreach.hangman.ui.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.developersbreach.hangman.repository.database.entity.HistoryEntity

/**
 * History screen, can be navigated from onboarding screen.
 * This screen has it's own ViewModel [HistoryViewModel]
 */
@Composable
fun HistoryScreen(
    navigateUp: () -> Unit,
    viewModel: HistoryViewModel
) {
    // Create ViewModel instance with koin.
    // Get all the game history list.
    val gameHistoryList by viewModel.gameHistoryList.observeAsState(emptyList())

    HistoryScreenUI(
        navigateUp = navigateUp,
        gameHistoryList = gameHistoryList,
        deleteAllGameHistoryData = { viewModel.deleteAllGameHistoryData() },
        onClickDeleteSelectedGameHistory = { history: HistoryEntity ->
            viewModel.deleteSelectedGameHistory(history)
        }
    )
}