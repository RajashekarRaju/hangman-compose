package com.developersbreach.hangman.logging

data class LoggingInitializationConfig(
    val minLevel: LogLevel,
    val sentryDsn: String? = null,
    val sentryEnabled: Boolean = false,
)

fun initializeLogging(config: LoggingInitializationConfig) {
    LogConfig.minLevel = config.minLevel
    LogConfig.sentryDsn = config.sentryDsn
    LogConfig.sentryEnabled = config.sentryEnabled
}
