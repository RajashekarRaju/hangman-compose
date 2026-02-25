package com.developersbreach.hangman.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.developersbreach.game.core.GameCategory
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_category_hint_multi_word
import com.developersbreach.hangman.feature.game.generated.resources.game_category_hint_single_word
import com.developersbreach.hangman.feature.game.generated.resources.game_category_singular_animal
import com.developersbreach.hangman.feature.game.generated.resources.game_category_singular_company
import com.developersbreach.hangman.feature.game.generated.resources.game_category_singular_country
import com.developersbreach.hangman.feature.game.generated.resources.game_category_singular_language
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun GameCategoryHintText(
    uiState: GameUiState,
    alpha: Float = 0.72f,
    modifier: Modifier = Modifier,
) {
    val word = uiState.wordToGuess.trim()
    if (word.isEmpty()) return

    val letterCount = word.count { !it.isWhitespace() }
    val wordCount = word.split(Regex("\\s+")).count { it.isNotBlank() }
    if (letterCount <= 0) return

    val categoryNoun = when (uiState.gameCategory) {
        GameCategory.COUNTRIES -> stringResource(Res.string.game_category_singular_country)
        GameCategory.LANGUAGES -> stringResource(Res.string.game_category_singular_language)
        GameCategory.COMPANIES -> stringResource(Res.string.game_category_singular_company)
        GameCategory.ANIMALS -> stringResource(Res.string.game_category_singular_animal)
    }

    val text = when {
        wordCount > 1 ->
            stringResource(
                Res.string.game_category_hint_multi_word,
                letterCount,
                categoryNoun,
                wordCount
            )

        else -> stringResource(Res.string.game_category_hint_single_word, letterCount, categoryNoun)
    }

    TitleLargeText(
        text = text,
        textAlign = TextAlign.Center,
        color = HangmanTheme.colorScheme.primary.copy(alpha = alpha),
        modifier = modifier,
    )
}
