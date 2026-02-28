package com.developersbreach.hangman.ui.game

import kotlin.js.JsName

@JsName("Date")
private external class JsDate {
    companion object {
        fun now(): Double
    }
}

internal actual fun nowEpochMillis(): Long = JsDate.now().toLong()
