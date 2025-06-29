package com.developersbreach.hangman.desktop.game

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.developersbreach.hangman.modal.Alphabets
import com.developersbreach.hangman.utils.ApplyAnimatedVisibility
import com.developersbreach.hangman.utils.SparkAnimateGuessedLetter

@Composable
fun AlphabetsGrid(
    modifier: Modifier = Modifier,
    alphabets: List<Alphabets>,
    onAlphabetClick: (Alphabets) -> Unit
) {
    Box(modifier = modifier) {
        ApplyAnimatedVisibility(densityValue = 400.dp) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(40.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(alphabets, key = { it.alphabetId }) { alphabet ->
                    AlphabetItem(alphabet, onAlphabetClick)
                }
            }
        }
    }
}

@Composable
private fun AlphabetItem(alphabet: Alphabets, onAlphabetClick: (Alphabets) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .alpha(if (!alphabet.isAlphabetGuessed) 1f else 0.25f)
            .clip(CircleShape)
            .size(40.dp)
            .background(MaterialTheme.colors.primary.copy(0.12f))
            .clickable(enabled = !alphabet.isAlphabetGuessed) { onAlphabetClick(alphabet) }
    ) {
        val (text, anim) = createRefs()

        if (alphabet.isAlphabetGuessed) {
            Box(modifier = Modifier.constrainAs(anim) { centerTo(parent) }) {
                SparkAnimateGuessedLetter()
            }
        }

        Text(
            text = alphabet.alphabet,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(text) { centerTo(parent) }
        )
    }
}
