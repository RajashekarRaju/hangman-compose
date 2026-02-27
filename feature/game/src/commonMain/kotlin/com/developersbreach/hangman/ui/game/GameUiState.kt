package com.developersbreach.hangman.ui.game

import com.developersbreach.game.core.Alphabet
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.HintError
import com.developersbreach.game.core.HintType
import com.developersbreach.game.core.LEVELS_PER_GAME
import com.developersbreach.game.core.MAX_ATTEMPTS_PER_LEVEL
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_unavailable_game_over
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_unavailable_no_elimination
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_unavailable_no_hints
import com.developersbreach.hangman.feature.game.generated.resources.game_hint_unavailable_no_letters
import com.developersbreach.hangman.feature.game.generated.resources.game_category_hint_multi_word
import com.developersbreach.hangman.feature.game.generated.resources.game_category_hint_single_word
import com.developersbreach.hangman.feature.game.generated.resources.game_category_singular_animal
import com.developersbreach.hangman.feature.game.generated.resources.game_category_singular_company
import com.developersbreach.hangman.feature.game.generated.resources.game_category_singular_country
import com.developersbreach.hangman.feature.game.generated.resources.game_category_singular_language
import org.jetbrains.compose.resources.StringResource

data class GameUiState(
    val alphabetsList: List<Alphabet> = emptyList(),
    val playerGuesses: List<String> = emptyList(),
    val gameOverByWinning: Boolean = false,
    val revealGuessingWord: Boolean = false,
    val wordToGuess: String = "",
    val attemptsLeftToGuess: Int = MAX_ATTEMPTS_PER_LEVEL,
    val pointsScoredOverall: Int = 0,
    val currentPlayerLevel: Int = 0,
    val maxLevelReached: Int = LEVELS_PER_GAME,
    val gameDifficulty: GameDifficulty = GameDifficulty.EASY,
    val gameCategory: GameCategory = GameCategory.COUNTRIES,
    val showInstructionsDialog: Boolean = false,
    val showExitDialog: Boolean = false,
    val showHintFeedbackDialog: Boolean = false,
    val hintFeedback: HintFeedback? = null,
    val hintsRemaining: Int = 1,
    val hintsUsedTotal: Int = 0,
    val hintTypesUsed: Set<HintType> = emptySet(),
    val isHintOnCooldown: Boolean = false,
    val levelTimeTotalMillis: Long = 60_000L,
    val levelTimeRemainingMillis: Long = 60_000L,
    val categoryHint: GameCategoryHintUiModel? = null,
)

data class HintFeedback(
    val selectedHintType: HintType,
    val error: HintError? = null,
)

data class GameCategoryHintUiModel(
    val templateRes: StringResource,
    val letterCount: Int,
    val categoryNounRes: StringResource,
    val wordCount: Int? = null,
)

val GameUiState.levelTimeProgress: Float
    get() = when {
        levelTimeTotalMillis <= 0L -> 0f
        else -> (levelTimeRemainingMillis.toFloat() / levelTimeTotalMillis.toFloat()).coerceIn(0f, 1f)
    }

val GameUiState.displayedLevel: Int
    get() = resolveDisplayedLevel(currentPlayerLevel, maxLevelReached)

internal fun resolveDisplayedLevel(
    currentPlayerLevel: Int,
    maxLevelReached: Int,
): Int {
    return if (currentPlayerLevel < maxLevelReached) currentPlayerLevel + 1 else maxLevelReached
}

internal fun levelProgress(currentPlayerLevel: Int): Float {
    return (currentPlayerLevel.coerceIn(0, LEVELS_PER_GAME).toFloat() / LEVELS_PER_GAME.toFloat())
        .coerceIn(0f, 1f)
}

internal fun attemptsUsedProgress(attemptsLeft: Int): Float {
    val clampedAttemptsLeft = attemptsLeft.coerceIn(0, MAX_ATTEMPTS_PER_LEVEL)
    val attemptsUsed = MAX_ATTEMPTS_PER_LEVEL - clampedAttemptsLeft
    return (attemptsUsed.toFloat() / MAX_ATTEMPTS_PER_LEVEL.toFloat()).coerceIn(0f, 1f)
}

internal fun HintError.messageRes(): StringResource {
    return when (this) {
        HintError.NO_HINTS_REMAINING -> Res.string.game_hint_unavailable_no_hints
        HintError.NO_UNREVEALED_LETTERS -> Res.string.game_hint_unavailable_no_letters
        HintError.NO_ELIMINATION_CANDIDATES -> Res.string.game_hint_unavailable_no_elimination
        HintError.GAME_ALREADY_FINISHED -> Res.string.game_hint_unavailable_game_over
    }
}

internal fun buildGameCategoryHintUiModel(
    wordToGuess: String,
    category: GameCategory,
): GameCategoryHintUiModel? {
    val normalizedWord = wordToGuess.trim()
    if (normalizedWord.isEmpty()) return null

    val letterCount = normalizedWord.count { !it.isWhitespace() }
    if (letterCount <= 0) return null

    val wordCount = normalizedWord.split(Regex("\\s+")).count { it.isNotBlank() }

    val categoryNounRes = when (category) {
        GameCategory.COUNTRIES -> Res.string.game_category_singular_country
        GameCategory.LANGUAGES -> Res.string.game_category_singular_language
        GameCategory.COMPANIES -> Res.string.game_category_singular_company
        GameCategory.ANIMALS -> Res.string.game_category_singular_animal
    }

    return when {
        wordCount > 1 -> GameCategoryHintUiModel(
            templateRes = Res.string.game_category_hint_multi_word,
            letterCount = letterCount,
            categoryNounRes = categoryNounRes,
            wordCount = wordCount,
        )

        else -> GameCategoryHintUiModel(
            templateRes = Res.string.game_category_hint_single_word,
            letterCount = letterCount,
            categoryNounRes = categoryNounRes,
            wordCount = null,
        )
    }
}
