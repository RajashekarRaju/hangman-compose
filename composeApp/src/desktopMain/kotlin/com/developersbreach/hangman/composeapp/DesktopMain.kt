package com.developersbreach.hangman.composeapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.developersbreach.hangman.composeapp.generated.resources.Res
import com.developersbreach.hangman.composeapp.generated.resources.compose_app_window_title
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    initKoinIfNeeded()
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(WindowPlacement.Maximized),
        title = stringResource(Res.string.compose_app_window_title),
    ) {
        HangmanRoot(closeApplication = ::exitApplication)
    }
}
