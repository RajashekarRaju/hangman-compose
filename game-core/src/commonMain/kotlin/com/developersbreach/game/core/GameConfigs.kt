package com.developersbreach.game.core

const val LEVELS_PER_GAME = 5
const val MAX_ATTEMPTS_PER_LEVEL = 8
const val DEFAULT_HINT_ELIMINATION_COUNT = 4

enum class GameDifficulty {
    EASY, MEDIUM, HARD, VERY_HARD
}

enum class GameCategory {
    COUNTRIES, LANGUAGES, COMPANIES, ANIMALS
}

val WORD_LENGTH_RANGE_BY_DIFFICULTY = mapOf(
    GameDifficulty.EASY to 4..5,
    GameDifficulty.MEDIUM to 6..7,
    GameDifficulty.HARD to 8..10,
    GameDifficulty.VERY_HARD to 11..Int.MAX_VALUE,
)

val REQUIRED_DIFFICULTY_COVERAGE = setOf(
    GameDifficulty.EASY,
    GameDifficulty.MEDIUM,
    GameDifficulty.HARD,
)

fun GameDifficulty.wordLengthRange(): IntRange {
    return WORD_LENGTH_RANGE_BY_DIFFICULTY.getValue(this)
}

fun String.playableLetterCount(): Int {
    return count { character -> character.isLetter() }
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
        GameDifficulty.VERY_HARD -> 1
    }
}