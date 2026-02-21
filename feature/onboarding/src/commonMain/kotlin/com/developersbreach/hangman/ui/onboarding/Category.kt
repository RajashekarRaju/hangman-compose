package com.developersbreach.hangman.ui.onboarding

import androidx.compose.runtime.Composable
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.feature.onboarding.generated.resources.Res
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_category_companies
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_category_countries
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_category_languages
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_difficulty_easy
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_difficulty_hard
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_difficulty_medium
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
            categoryName = stringResource(Res.string.onboarding_category_countries)
        ),
        Category(
            category = GameCategory.LANGUAGES,
            categoryName = stringResource(Res.string.onboarding_category_languages)
        ),
        Category(
            category = GameCategory.COMPANIES,
            categoryName = stringResource(Res.string.onboarding_category_companies)
        )
    )
}

@Composable
internal fun categoryName(gameCategory: GameCategory): String {
    return when (gameCategory) {
        GameCategory.COUNTRIES -> stringResource(Res.string.onboarding_category_countries)
        GameCategory.LANGUAGES -> stringResource(Res.string.onboarding_category_languages)
        GameCategory.COMPANIES -> stringResource(Res.string.onboarding_category_companies)
    }
}

@Composable
internal fun gameDifficultyName(gameDifficulty: GameDifficulty): String {
    return when (gameDifficulty) {
        GameDifficulty.EASY -> stringResource(Res.string.onboarding_difficulty_easy)
        GameDifficulty.MEDIUM -> stringResource(Res.string.onboarding_difficulty_medium)
        GameDifficulty.HARD -> stringResource(Res.string.onboarding_difficulty_hard)
    }
}