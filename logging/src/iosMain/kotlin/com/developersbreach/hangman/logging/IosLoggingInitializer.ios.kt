package com.developersbreach.hangman.logging

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.Platform
import platform.Foundation.NSBundle

@OptIn(ExperimentalNativeApi::class)
fun initializeIosLoggingFromRuntime() {
    val sentryDsn = (NSBundle.mainBundle.objectForInfoDictionaryKey("SENTRY_DSN") as? String)
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
    val isDebugBinary = Platform.isDebugBinary
    initializeLogging(
        LoggingInitializationConfig(
            minLevel = if (isDebugBinary) LogLevel.DEBUG else LogLevel.NONE,
            sentryDsn = sentryDsn,
            sentryEnabled = !isDebugBinary && !sentryDsn.isNullOrBlank(),
        ),
    )
}
