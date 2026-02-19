package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun GuessedAlphabetsContainer(
    playerGuesses: List<String>,
    modifier: Modifier,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        items(items = playerGuesses) { validGuess ->
            ItemGuessingAlphabetContainer(validGuess)
        }
    }
}

@Composable
private fun ItemGuessingAlphabetContainer(validGuess: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(HangmanTheme.colorScheme.primary.copy(alpha = 0.10f))
            .padding(20.dp),
    ) {
        TitleMediumText(
            text = validGuess,
            color = HangmanTheme.colorScheme.onBackground,
        )
    }
}
