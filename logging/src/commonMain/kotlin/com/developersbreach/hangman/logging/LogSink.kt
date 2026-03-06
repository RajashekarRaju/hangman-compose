package com.developersbreach.hangman.logging

fun interface LogSink {
    fun log(event: LogEvent)
}

object PlatformLogSink : LogSink {

    override fun log(event: LogEvent) {
        platformLog(event)
    }
}
