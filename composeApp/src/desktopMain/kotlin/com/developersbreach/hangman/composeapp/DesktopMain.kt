package com.developersbreach.hangman.composeapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.developersbreach.hangman.navigation.AppNavigation
import com.developersbreach.hangman.ui.theme.HangmanTheme

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Hangman",
    ) {
        HangmanTheme {
            AppNavigation(closeApplication = ::exitApplication)
        }
    }
}
