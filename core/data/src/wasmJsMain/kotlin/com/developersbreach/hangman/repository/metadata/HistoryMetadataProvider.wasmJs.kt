package com.developersbreach.hangman.repository.metadata

import kotlin.js.JsName

actual fun generateHistoryMetadata(historySize: Int): HistoryMetadata {
    return HistoryMetadata(
        gameId = "web-${jsTimestamp()}-${historySize + 1}",
        gamePlayedDate = JsDate().toLocaleDateString(),
        gamePlayedTime = JsDate().toLocaleTimeString(),
    )
}

@JsName("Date")
private external class JsDate {
    constructor()

    fun toLocaleDateString(): String
    fun toLocaleTimeString(): String

    companion object {
        fun now(): Double
    }
}

private fun jsTimestamp(): String = JsDate.now().toLong().toString()
