package com.developersbreach.hangman.composeapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.developersbreach.hangman.navigation.AppNavigation
import com.developersbreach.hangman.ui.theme.HangmanTheme

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(
        content = {
            HangmanTheme {
                AppNavigation(
                    closeApplication = {
                        // NO need to do anything here.
                    }
                )
            }
        }
    )
}
