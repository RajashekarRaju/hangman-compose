package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.developersbreach.game.core.Alphabet
import com.developersbreach.hangman.ui.components.SparkPulse
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.components.creepyOutline
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun AlphabetsList(
    modifier: Modifier,
    alphabetsList: List<Alphabet>,
    tileSize: Dp,
    spacing: Dp,
    contentPadding: Dp,
    creepinessThreshold: Float = 0.16f,
    onAlphabetClicked: (alphabetId: Int) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(tileSize),
            horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterVertically),
            contentPadding = PaddingValues(
                start = contentPadding,
                top = contentPadding,
                end = contentPadding,
                bottom = contentPadding + 24.dp,
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(items = alphabetsList, key = { it.alphabetId }) { alphabet ->
                ItemAlphabetText(
                    alphabet = alphabet,
                    onAlphabetClicked = onAlphabetClicked,
                    tileSize = tileSize,
                    creepinessThreshold = creepinessThreshold,
                )
            }
        }
    }
}

@Composable
private fun ItemAlphabetText(
    alphabet: Alphabet,
    onAlphabetClicked: (alphabetId: Int) -> Unit,
    tileSize: Dp,
    creepinessThreshold: Float,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .alpha(if (!alphabet.isAlphabetGuessed) 1f else 0.25f)
            .size(tileSize)
            .creepyOutline(
                seed = alphabet.alphabetId,
                threshold = creepinessThreshold,
                fillColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.10f),
                outlineColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.25f),
            )
            .clickable(
                enabled = !alphabet.isAlphabetGuessed,
                onClick = { onAlphabetClicked(alphabet.alphabetId) },
            ),
    ) {
        if (alphabet.isAlphabetGuessed) {
            SparkPulse(
                modifier = Modifier.size(tileSize),
                sparkColor = HangmanTheme.colorScheme.primary.copy(alpha = 0.50f),
            )
        }

        TitleLargeText(
            text = alphabet.alphabet,
            color = HangmanTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
    }
}
