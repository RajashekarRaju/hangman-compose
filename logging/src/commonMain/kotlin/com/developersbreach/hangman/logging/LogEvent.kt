package com.developersbreach.hangman.logging

data class LogEvent(
    val level: LogLevel,
    val tag: String,
    val message: String,
    val throwable: Throwable? = null,
)
