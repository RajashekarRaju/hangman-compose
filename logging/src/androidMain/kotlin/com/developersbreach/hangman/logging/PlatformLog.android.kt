package com.developersbreach.hangman.logging

import android.util.Log

actual fun platformLog(event: LogEvent) {

    when (event.level) {
        LogLevel.DEBUG -> when (event.throwable) {
            null -> Log.d(event.tag, event.message)
            else -> Log.d(event.tag, event.message, event.throwable)
        }

        LogLevel.INFO -> when (event.throwable) {
            null -> Log.i(event.tag, event.message)
            else -> Log.i(event.tag, event.message, event.throwable)
        }

        LogLevel.WARN -> when (event.throwable) {
            null -> Log.w(event.tag, event.message)
            else -> Log.w(event.tag, event.message, event.throwable)
        }

        LogLevel.ERROR -> when (event.throwable) {
            null -> Log.e(event.tag, event.message)
            else -> Log.e(event.tag, event.message, event.throwable)
        }

        LogLevel.NONE -> Unit
    }
}
