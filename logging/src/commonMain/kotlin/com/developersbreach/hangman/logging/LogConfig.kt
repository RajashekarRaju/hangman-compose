package com.developersbreach.hangman.logging

object LogConfig {

    var minLevel: LogLevel = LogLevel.INFO
    var sink: LogSink = PlatformLogSink
    var sentryDsn: String? = null
        set(value) {
            field = value?.trim()?.takeIf { it.isNotEmpty() }
            configureSentry(sentryEnabled, field)
        }
    var sentryEnabled: Boolean = false
        set(value) {
            field = value
            configureSentry(field, sentryDsn)
        }

    fun reset() {
        minLevel = LogLevel.INFO
        sink = PlatformLogSink
        sentryEnabled = false
        sentryDsn = null
    }
}