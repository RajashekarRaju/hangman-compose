package com.developersbreach.game.core

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
