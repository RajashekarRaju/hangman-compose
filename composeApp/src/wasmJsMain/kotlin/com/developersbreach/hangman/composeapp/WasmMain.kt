package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.developersbreach.hangman.logging.LoggingInitializationConfig
import com.developersbreach.hangman.logging.LogLevel
import com.developersbreach.hangman.logging.initializeLogging
import kotlinx.browser.document
import org.koin.core.context.startKoin

@JsName("hangmanReadGlobalString")
private external fun hangmanReadGlobalString(name: String): String?

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initWebLogging()
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

private fun initWebLogging() {
    val dsn = readGlobalString("HANGMAN_SENTRY_DSN")
    initializeLogging(
        LoggingInitializationConfig(
            minLevel = LogLevel.INFO,
            sentryDsn = dsn,
            sentryEnabled = !dsn.isNullOrBlank(),
        ),
    )
}

@Suppress("UnsafeCastFromDynamic")
private fun readGlobalString(name: String): String? {
    return hangmanReadGlobalString(name)
}