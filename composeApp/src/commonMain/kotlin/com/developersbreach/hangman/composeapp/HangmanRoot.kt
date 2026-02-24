package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.developersbreach.hangman.navigation.AppNavigation
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePalettes
import org.koin.core.context.startKoin
import org.koin.compose.viewmodel.koinViewModel

private var koinInitialized = false

fun initKoinIfNeeded() {
    if (!koinInitialized) {
        startKoin {
            modules(initKoinComponents())
        }
        koinInitialized = true
    }
}

@Composable
fun HangmanRoot(closeApplication: () -> Unit) {
    val viewModel = koinViewModel<AppInitializerViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val skullCursorEnabled = isCustomSkullCursorSupported()

    HangmanTheme(palette = ThemePalettes.byId(uiState.themePaletteId)) {
        ThemedSkullCursorContainer(enabled = skullCursorEnabled) {
            AppNavigation(closeApplication = closeApplication)
        }
    }
}
