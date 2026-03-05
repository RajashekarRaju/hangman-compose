package com.developersbreach.hangman.repository

enum class ThemeMode {
    LIGHT,
    DARK;

    companion object {
        val default: ThemeMode = DARK

        fun fromStorage(value: String): ThemeMode {
            return entries.firstOrNull { mode -> mode.name == value } ?: default
        }
    }
}
