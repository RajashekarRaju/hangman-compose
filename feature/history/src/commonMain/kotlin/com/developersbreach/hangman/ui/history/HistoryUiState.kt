package com.developersbreach.hangman.ui.history

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.HintType
import com.developersbreach.hangman.feature.common.ui.generated.resources.Res as CommonRes
import com.developersbreach.hangman.feature.common.ui.generated.resources.category_animals
import com.developersbreach.hangman.feature.common.ui.generated.resources.category_companies
import com.developersbreach.hangman.feature.common.ui.generated.resources.category_countries
import com.developersbreach.hangman.feature.common.ui.generated.resources.category_languages
import com.developersbreach.hangman.feature.common.ui.generated.resources.difficulty_easy
import com.developersbreach.hangman.feature.common.ui.generated.resources.difficulty_hard
import com.developersbreach.hangman.feature.common.ui.generated.resources.difficulty_medium
import com.developersbreach.hangman.feature.common.ui.generated.resources.difficulty_very_hard
import com.developersbreach.hangman.repository.model.HistoryRecord
import com.developersbreach.game.core.LEVELS_PER_GAME
import com.developersbreach.hangman.feature.history.generated.resources.Res as HistoryRes
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
    val hintTypeLabelRes: List<StringResource>,
)

fun HistoryRecord.toHistoryListItemUiState(): HistoryListItemUiState {
    return HistoryListItemUiState(
        history = this,
        levelProgress = historyLevelProgress(gameLevel),
        hintTypeLabelRes = hintTypesUsed.map { hintType -> hintType.labelRes() },
    )
}

private fun historyLevelProgress(level: Int): Float {
    if (level <= 0) return 0f
    return (level.coerceAtMost(LEVELS_PER_GAME)) / LEVELS_PER_GAME.toFloat()
}

internal fun GameDifficulty.labelRes(): StringResource {
    return when (this) {
        GameDifficulty.EASY -> CommonRes.string.difficulty_easy
        GameDifficulty.MEDIUM -> CommonRes.string.difficulty_medium
        GameDifficulty.HARD -> CommonRes.string.difficulty_hard
        GameDifficulty.VERY_HARD -> CommonRes.string.difficulty_very_hard
    }
}

internal fun GameCategory.labelRes(): StringResource {
    return when (this) {
        GameCategory.COUNTRIES -> CommonRes.string.category_countries
        GameCategory.LANGUAGES -> CommonRes.string.category_languages
        GameCategory.COMPANIES -> CommonRes.string.category_companies
        GameCategory.ANIMALS -> CommonRes.string.category_animals
    }
}

internal fun HintType.labelRes(): StringResource {
    return when (this) {
        HintType.REVEAL_LETTER -> HistoryRes.string.history_hint_type_reveal_letter
        HintType.ELIMINATE_LETTERS -> HistoryRes.string.history_hint_type_eliminate_letters
    }
}
