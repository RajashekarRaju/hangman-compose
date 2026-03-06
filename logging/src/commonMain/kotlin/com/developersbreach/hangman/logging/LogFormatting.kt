package com.developersbreach.hangman.logging

internal fun formatLogMessage(
    event: LogEvent
): String {
    val throwableText = event.throwable?.let { " | ${it::class.simpleName}: ${it.message}" } ?: ""
    return "[${event.level.name}] ${event.tag}: ${event.message}$throwableText"
}