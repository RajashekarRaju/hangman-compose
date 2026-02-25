package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(initKoinComponents())
    }

    ComposeViewport(viewportContainerId = "compose-root") {
        SideEffect {
            document.getElementById("boot-loader")?.classList?.add("hidden")
        }
        HangmanRoot(closeApplication = { })
    }
}
