package com.developersbreach.hangman.composeapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.developersbreach.hangman.navigation.AppNavigation
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(initKoinComponents())
    }

    ComposeViewport(viewportContainerId = "compose-root") {
        HangmanTheme {
            AppNavigation(
                closeApplication = {
                    // NO need to do anything here.
                }
            )
        }
    }
}
