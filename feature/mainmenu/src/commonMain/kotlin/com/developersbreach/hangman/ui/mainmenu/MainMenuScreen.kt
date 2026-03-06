package com.developersbreach.hangman.ui.mainmenu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import com.developersbreach.hangman.ui.preview.HangmanScreenPreviews
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MainMenuScreen(
    navigateToGameScreen: () -> Unit,
    navigateToSettingsScreen: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    navigateToAchievementsScreen: () -> Unit,
    navigateToGameGuideScreen: () -> Unit,
    viewModel: MainMenuViewModel,
    finishActivity: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                MainMenuEffect.NavigateToGame -> navigateToGameScreen()
                MainMenuEffect.NavigateToSettings -> navigateToSettingsScreen()
                MainMenuEffect.NavigateToHistory -> navigateToHistoryScreen()
                MainMenuEffect.NavigateToAchievements -> navigateToAchievementsScreen()
                MainMenuEffect.NavigateToGameGuide -> navigateToGameGuideScreen()
                MainMenuEffect.FinishActivity -> finishActivity()
                is MainMenuEffect.OpenIssueTracker -> uriHandler.openUri(effect.url)
            }
        }
    }

    uiState.MainMenuScreenUI(
        onEvent = viewModel::onEvent
    )
}

@HangmanScreenPreviews
@Composable
private fun MainMenuScreenUIPreview() {
    HangmanTheme(
        darkTheme = true,
        palette = ThemePalettes.byId(ThemePaletteId.INSANE_RED)
    ) {
        MainMenuUiState(
            highScore = 1520,
            hasUnreadAchievements = true,
        ).MainMenuScreenUI(
            onEvent = {},
        )
    }
}
