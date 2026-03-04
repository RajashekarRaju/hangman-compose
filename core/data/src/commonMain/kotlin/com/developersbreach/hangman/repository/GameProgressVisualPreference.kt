package com.developersbreach.hangman.repository

enum class GameProgressVisualPreference {
    LEVEL_POINTS_ATTEMPTS,
    TRADITIONAL_HANGMAN;

    companion object {
        val default: GameProgressVisualPreference = TRADITIONAL_HANGMAN

        fun fromStorage(value: String): GameProgressVisualPreference {
            return entries.firstOrNull { entry -> entry.name == value } ?: default
        }
    }
}
