package com.developersbreach.hangman.composeapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    initKoinIfNeeded()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Hangman",
    ) {
        HangmanRoot(closeApplication = ::exitApplication)
    }
}
