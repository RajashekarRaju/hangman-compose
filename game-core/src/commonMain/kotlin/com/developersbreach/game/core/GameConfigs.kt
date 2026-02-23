package com.developersbreach.game.core

const val LEVELS_PER_GAME = 5
const val MAX_ATTEMPTS_PER_LEVEL = 8
const val DEFAULT_HINT_ELIMINATION_COUNT = 4

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

enum class HintType {
    REVEAL_LETTER,
    ELIMINATE_LETTERS,
}

enum class HintError {
    NO_HINTS_REMAINING,
    NO_UNREVEALED_LETTERS,
    NO_ELIMINATION_CANDIDATES,
    GAME_ALREADY_FINISHED,
}

fun hintsPerLevelForDifficulty(difficulty: GameDifficulty): Int {
    return when (difficulty) {
        GameDifficulty.EASY -> 2
        GameDifficulty.MEDIUM -> 1
        GameDifficulty.HARD -> 1
    }
}