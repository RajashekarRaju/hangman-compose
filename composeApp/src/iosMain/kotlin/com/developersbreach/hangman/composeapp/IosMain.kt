package com.developersbreach.hangman.composeapp

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {

    initKoinIfNeeded()
    return ComposeUIViewController {
        HangmanRoot(closeApplication = {})
    }
}
