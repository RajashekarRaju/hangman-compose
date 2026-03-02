package com.developersbreach.hangman.logging

actual fun platformLog(event: LogEvent) {
    println(formatLogMessage(event))
    // NSLog("%@", formatLogMessage(event))
}
