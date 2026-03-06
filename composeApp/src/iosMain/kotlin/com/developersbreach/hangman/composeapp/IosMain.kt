package com.developersbreach.hangman.composeapp

import androidx.compose.ui.window.ComposeUIViewController
import com.developersbreach.hangman.logging.initializeIosLoggingFromRuntime
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    initializeIosLoggingFromRuntime()
    initKoinIfNeeded()
    return ComposeUIViewController {
        HangmanRoot(closeApplication = {})
    }
}
