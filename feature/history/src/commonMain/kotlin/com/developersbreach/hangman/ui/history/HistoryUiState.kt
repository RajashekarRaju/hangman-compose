package com.developersbreach.hangman.ui.history

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.HintType
import com.developersbreach.hangman.repository.model.HistoryRecord
import com.developersbreach.game.core.LEVELS_PER_GAME
import com.developersbreach.hangman.feature.history.generated.resources.Res
import com.developersbreach.hangman.feature.history.generated.resources.history_category_companies
import com.developersbreach.hangman.feature.history.generated.resources.history_category_countries
import com.developersbreach.hangman.feature.history.generated.resources.history_category_languages
import com.developersbreach.hangman.feature.history.generated.resources.history_difficulty_easy
import com.developersbreach.hangman.feature.history.generated.resources.history_difficulty_hard
import com.developersbreach.hangman.feature.history.generated.resources.history_difficulty_medium
import com.developersbreach.hangman.feature.history.generated.resources.history_hint_type_eliminate_letters
import com.developersbreach.hangman.feature.history.generated.resources.history_hint_type_reveal_letter
import org.jetbrains.compose.resources.StringResource

data class HistoryUiState(
    val gameHistoryList: List<HistoryListItemUiState> = emptyList()
) {
    val showDeleteIconInAppBar: Boolean
        get() = gameHistoryList.isNotEmpty()
}

data class HistoryListItemUiState(
    val history: HistoryRecord,
    val levelProgress: Float,
)

fun HistoryRecord.toHistoryListItemUiState(): HistoryListItemUiState {
    return HistoryListItemUiState(
        history = this,
        levelProgress = historyLevelProgress(gameLevel),
    )
}

private fun historyLevelProgress(level: Int): Float {
    if (level <= 0) return 0f
    return (level.coerceAtMost(LEVELS_PER_GAME)) / LEVELS_PER_GAME.toFloat()
}

internal fun GameDifficulty.labelRes(): StringResource {
    return when (this) {
        GameDifficulty.EASY -> Res.string.history_difficulty_easy
        GameDifficulty.MEDIUM -> Res.string.history_difficulty_medium
        GameDifficulty.HARD -> Res.string.history_difficulty_hard
    }
}

internal fun GameCategory.labelRes(): StringResource {
    return when (this) {
        GameCategory.COUNTRIES -> Res.string.history_category_countries
        GameCategory.LANGUAGES -> Res.string.history_category_languages
        GameCategory.COMPANIES -> Res.string.history_category_companies
    }
}

internal fun HintType.labelRes(): StringResource {
    return when (this) {
        HintType.REVEAL_LETTER -> Res.string.history_hint_type_reveal_letter
        HintType.ELIMINATE_LETTERS -> Res.string.history_hint_type_eliminate_letters
    }
}
