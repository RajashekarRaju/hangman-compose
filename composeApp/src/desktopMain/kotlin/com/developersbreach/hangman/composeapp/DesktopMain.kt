package com.developersbreach.hangman.composeapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    initKoinIfNeeded()
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(WindowPlacement.Maximized),
        title = "Hangman",
    ) {
        HangmanRoot(closeApplication = ::exitApplication)
    }
}
