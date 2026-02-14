package com.developersbreach.hangman.ui.game

import androidx.compose.runtime.Composable
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_category_companies
import com.developersbreach.hangman.feature.game.generated.resources.game_category_countries
import com.developersbreach.hangman.feature.game.generated.resources.game_category_languages
import com.developersbreach.hangman.feature.game.generated.resources.game_difficulty_easy
import com.developersbreach.hangman.feature.game.generated.resources.game_difficulty_hard
import com.developersbreach.hangman.feature.game.generated.resources.game_difficulty_medium
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun difficultyLabel(gameDifficulty: GameDifficulty): String {
    return when (gameDifficulty) {
        GameDifficulty.EASY -> stringResource(Res.string.game_difficulty_easy)
        GameDifficulty.MEDIUM -> stringResource(Res.string.game_difficulty_medium)
        GameDifficulty.HARD -> stringResource(Res.string.game_difficulty_hard)
    }
}

@Composable
internal fun categoryLabel(gameCategory: GameCategory): String {
    return when (gameCategory) {
        GameCategory.COUNTRIES -> stringResource(Res.string.game_category_countries)
        GameCategory.LANGUAGES -> stringResource(Res.string.game_category_languages)
        GameCategory.COMPANIES -> stringResource(Res.string.game_category_companies)
    }
}
