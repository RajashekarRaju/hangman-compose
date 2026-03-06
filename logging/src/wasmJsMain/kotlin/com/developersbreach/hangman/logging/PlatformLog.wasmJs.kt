package com.developersbreach.hangman.logging

import kotlin.js.JsName

private external interface Console {
    fun debug(message: String)
    fun info(message: String)
    fun warn(message: String)
    fun error(message: String)
}

@JsName("console")
private external val console: Console

private external interface DateApi {
    fun now(): Double
}

@JsName("Date")
private external val dateApi: DateApi

actual fun platformLog(event: LogEvent) {
    val time = currentTime()
    val header = "$time ${event.level.name} ${event.tag}: ${event.message}"
    val throwableIndent = " ".repeat(time.length + 1)
    val message = event.throwable?.let { throwable ->
        "$header\n$throwableIndent${throwable::class.simpleName}: ${throwable.message ?: "no-message"}"
    } ?: header

    when (event.level) {
        LogLevel.DEBUG -> console.debug(message)
        LogLevel.INFO -> console.info(message)
        LogLevel.WARN -> console.warn(message)
        LogLevel.ERROR -> console.error(message)
        LogLevel.NONE -> Unit
    }
}

private fun currentTime(): String {
    val nowMillis = dateApi.now().toLong()
    val totalSeconds = (nowMillis / 1000L) % 86_400L
    val hours = totalSeconds / 3_600L
    val minutes = (totalSeconds % 3_600L) / 60L
    val seconds = totalSeconds % 60L
    return "${hours.twoDigits()}:${minutes.twoDigits()}:${seconds.twoDigits()}"
}

private fun Long.twoDigits(): String = toString().padStart(2, '0')
