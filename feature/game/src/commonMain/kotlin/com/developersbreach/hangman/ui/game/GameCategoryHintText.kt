package com.developersbreach.hangman.ui.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun GameCategoryHintText(
    categoryHint: GameCategoryHintUiModel?,
    alpha: Float = 0.72f,
    modifier: Modifier = Modifier,
) {
    categoryHint ?: return
    val categoryNoun = stringResource(categoryHint.categoryNounRes)
    val text = when (val count = categoryHint.wordCount) {
        null -> stringResource(
            categoryHint.templateRes,
            categoryHint.letterCount,
            categoryNoun,
        )

        else -> stringResource(
            categoryHint.templateRes,
            categoryHint.letterCount,
            categoryNoun,
            count,
        )
    }

    TitleLargeText(
        text = text,
        textAlign = TextAlign.Center,
        color = HangmanTheme.colorScheme.primary.copy(alpha = alpha),
        modifier = modifier,
    )
}
