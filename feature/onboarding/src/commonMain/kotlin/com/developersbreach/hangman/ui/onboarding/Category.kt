package com.developersbreach.hangman.ui.onboarding

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

data class Category(
    val category: GameCategory,
    val categoryName: String
)

@Composable
internal fun categoryOptions(): List<Category> {
    return listOf(
        Category(
            category = GameCategory.COUNTRIES,
            categoryName = stringResource(Res.string.category_countries)
        ),
        Category(
            category = GameCategory.LANGUAGES,
            categoryName = stringResource(Res.string.category_languages)
        ),
        Category(
            category = GameCategory.COMPANIES,
            categoryName = stringResource(Res.string.category_companies)
        ),
        Category(
            category = GameCategory.ANIMALS,
            categoryName = stringResource(Res.string.category_animals)
        )
    )
}

@Composable
internal fun categoryName(gameCategory: GameCategory): String {
    return when (gameCategory) {
        GameCategory.COUNTRIES -> stringResource(Res.string.category_countries)
        GameCategory.LANGUAGES -> stringResource(Res.string.category_languages)
        GameCategory.COMPANIES -> stringResource(Res.string.category_companies)
        GameCategory.ANIMALS -> stringResource(Res.string.category_animals)
    }
}

@Composable
internal fun gameDifficultyName(gameDifficulty: GameDifficulty): String {
    return when (gameDifficulty) {
        GameDifficulty.EASY -> stringResource(Res.string.difficulty_easy)
        GameDifficulty.MEDIUM -> stringResource(Res.string.difficulty_medium)
        GameDifficulty.HARD -> stringResource(Res.string.difficulty_hard)
        GameDifficulty.VERY_HARD -> stringResource(Res.string.difficulty_very_hard)
    }
}