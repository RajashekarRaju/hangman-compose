package com.developersbreach.game.core

const val LEVELS_PER_GAME = 5
const val MAX_ATTEMPTS_PER_LEVEL = 8

enum class GameDifficulty {
    EASY, MEDIUM, HARD
}

enum class GameCategory {
    COUNTRIES, LANGUAGES, COMPANIES
}

data class Words(
    val wordName: String
)

data class Alphabet(
    val alphabetId: Int,
    val alphabet: String,
    val isAlphabetGuessed: Boolean = false
)
