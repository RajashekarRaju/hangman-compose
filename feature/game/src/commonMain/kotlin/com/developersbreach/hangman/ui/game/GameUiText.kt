package com.developersbreach.hangman.ui.game

import androidx.compose.runtime.Composable
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.feature.common.ui.generated.resources.Res
import com.developersbreach.hangman.feature.common.ui.generated.resources.category_animals
import com.developersbreach.hangman.feature.common.ui.generated.resources.category_companies
import com.developersbreach.hangman.feature.common.ui.generated.resources.category_countries
import com.developersbreach.hangman.feature.common.ui.generated.resources.category_languages
import com.developersbreach.hangman.feature.common.ui.generated.resources.difficulty_easy
import com.developersbreach.hangman.feature.common.ui.generated.resources.difficulty_hard
import com.developersbreach.hangman.feature.common.ui.generated.resources.difficulty_medium
import com.developersbreach.hangman.feature.common.ui.generated.resources.difficulty_very_hard
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun difficultyLabel(gameDifficulty: GameDifficulty): String {
    return when (gameDifficulty) {
        GameDifficulty.EASY -> stringResource(Res.string.difficulty_easy)
        GameDifficulty.MEDIUM -> stringResource(Res.string.difficulty_medium)
        GameDifficulty.HARD -> stringResource(Res.string.difficulty_hard)
        GameDifficulty.VERY_HARD -> stringResource(Res.string.difficulty_very_hard)
    }
}

@Composable
internal fun categoryLabel(gameCategory: GameCategory): String {
    return when (gameCategory) {
        GameCategory.COUNTRIES -> stringResource(Res.string.category_countries)
        GameCategory.LANGUAGES -> stringResource(Res.string.category_languages)
        GameCategory.COMPANIES -> stringResource(Res.string.category_companies)
        GameCategory.ANIMALS -> stringResource(Res.string.category_animals)
    }
}
