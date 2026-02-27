package com.developersbreach.hangman.ui.onboarding

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
import org.jetbrains.compose.resources.StringResource

internal val categoryOptions: List<GameCategory> = GameCategory.entries

internal fun GameCategory.labelRes(): StringResource {
    return when (this) {
        GameCategory.COUNTRIES -> Res.string.category_countries
        GameCategory.LANGUAGES -> Res.string.category_languages
        GameCategory.COMPANIES -> Res.string.category_companies
        GameCategory.ANIMALS -> Res.string.category_animals
    }
}

internal fun GameDifficulty.labelRes(): StringResource {
    return when (this) {
        GameDifficulty.EASY -> Res.string.difficulty_easy
        GameDifficulty.MEDIUM -> Res.string.difficulty_medium
        GameDifficulty.HARD -> Res.string.difficulty_hard
        GameDifficulty.VERY_HARD -> Res.string.difficulty_very_hard
    }
}