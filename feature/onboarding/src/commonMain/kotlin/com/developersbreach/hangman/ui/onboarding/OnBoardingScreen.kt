package com.developersbreach.hangman.ui.onboarding

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
fun OnBoardingScreen(
    navigateToGameScreen: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    viewModel: OnBoardingViewModel,
    finishActivity: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collectLatest { effect ->
            when (effect) {
                OnBoardingEffect.NavigateToGame -> navigateToGameScreen()
                OnBoardingEffect.NavigateToHistory -> navigateToHistoryScreen()
                OnBoardingEffect.FinishActivity -> finishActivity()
                is OnBoardingEffect.OpenIssueTracker -> uriHandler.openUri(effect.url)
            }
        }
    }

    uiState.OnBoardingScreenUI(
        onEvent = viewModel::onEvent
    )
}

@HangmanScreenPreviews
@Composable
private fun OnBoardingScreenUIPreview() {
    HangmanTheme(
        darkTheme = true,
        palette = ThemePalettes.byId(ThemePaletteId.INSANE_RED)
    ) {
        OnBoardingUiState(highScore = 1520).OnBoardingScreenUI(
            onEvent = {},
        )
    }
}
