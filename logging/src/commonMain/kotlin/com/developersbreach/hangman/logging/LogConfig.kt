package com.developersbreach.hangman.logging

object LogConfig {

    var minLevel: LogLevel = LogLevel.INFO
    var sink: LogSink = PlatformLogSink

    fun reset() {
        minLevel = LogLevel.INFO
        sink = PlatformLogSink
    }
}
