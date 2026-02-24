package com.developersbreach.hangman.composeapp

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(initKoinComponents())
    }

    ComposeViewport(viewportContainerId = "compose-root") {
        HangmanRoot(closeApplication = { })
    }
}
