package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.Alphabet
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun AlphabetsList(
    modifier: Modifier,
    alphabetsList: List<Alphabet>,
    onAlphabetClicked: (alphabetId: Int) -> Unit,
) {
    Box(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(40.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(items = alphabetsList, key = { it.alphabetId }) { alphabet ->
                ItemAlphabetText(
                    alphabet = alphabet,
                    onAlphabetClicked = onAlphabetClicked,
                )
            }
        }
    }
}

@Composable
private fun ItemAlphabetText(
    alphabet: Alphabet,
    onAlphabetClicked: (alphabetId: Int) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .alpha(if (!alphabet.isAlphabetGuessed) 1f else 0.25f)
            .clip(CircleShape)
            .size(40.dp)
            .background(color = HangmanTheme.colorScheme.primary.copy(alpha = 0.12f))
            .clickable(
                enabled = !alphabet.isAlphabetGuessed,
                onClick = { onAlphabetClicked(alphabet.alphabetId) },
            ),
    ) {
        TitleMediumText(
            text = alphabet.alphabet,
            color = HangmanTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
    }
}
