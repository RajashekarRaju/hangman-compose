package com.developersbreach.hangman.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.developersbreach.hangman.ui.preview.HangmanScreenPreviews
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsScreen(
    navigateUp: () -> Unit,
    viewModel: SettingsViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                SettingsEffect.NavigateUp -> navigateUp()
            }
        }
    }

    uiState.SettingsScreenUI(onEvent = viewModel::onEvent)
}

@HangmanScreenPreviews
@Composable
private fun SettingsScreenPreview() {
    HangmanTheme(
        darkTheme = true,
        palette = ThemePalettes.byId(ThemePaletteId.INSANE_RED)
    ) {
        SettingsUiState().SettingsScreenUI(
            onEvent = {},
        )
    }
}
