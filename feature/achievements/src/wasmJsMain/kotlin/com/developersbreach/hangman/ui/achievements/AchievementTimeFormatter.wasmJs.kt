package com.developersbreach.hangman.ui.achievements

import kotlin.js.JsName

@JsName("Date")
private external class JsDate {
    constructor(epochMillis: Double)

    fun toLocaleDateString(): String
    fun toLocaleTimeString(): String
}

internal actual fun formatEpochMillis(epochMillis: Long): String {
    val date = JsDate(epochMillis.toDouble())
    return "${date.toLocaleDateString()} ${date.toLocaleTimeString()}"
}
