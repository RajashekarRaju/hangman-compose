package com.developersbreach.hangman.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.developersbreach.hangman.shared.Greeting

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Hangman Desktop") {
        androidx.compose.material.Text(Greeting().greet())
    }
}
