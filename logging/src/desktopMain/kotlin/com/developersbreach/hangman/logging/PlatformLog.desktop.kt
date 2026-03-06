package com.developersbreach.hangman.logging

import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

actual fun platformLog(event: LogEvent) {
    val time = LocalTime.now().format(timeFormatter)
    val header = "$time ${event.level.name} ${event.tag}: ${event.message}"
    val throwableIndent = " ".repeat(time.length + 1)
    val line = event.throwable?.let { throwable ->
        "$header\n$throwableIndent${throwable::class.simpleName}: ${throwable.message ?: "no-message"}"
    } ?: header

    val shouldUseErrorStream = event.level == LogLevel.ERROR || event.throwable != null
    if (shouldUseErrorStream) {
        System.err.println(line)
    } else {
        System.out.println(line)
    }
}
