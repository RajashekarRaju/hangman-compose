package com.developersbreach.hangman.ui.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import kotlinx.coroutines.flow.collectLatest

/**
 * History screen, can be navigated from onboarding screen.
 * This screen has it's own ViewModel [HistoryViewModel]
 */
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

    HistoryScreenUI(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}