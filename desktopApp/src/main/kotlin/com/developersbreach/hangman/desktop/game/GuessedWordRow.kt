package com.developersbreach.hangman.desktop.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun GuessedWordRow(updateGuesses: SnapshotStateList<String>, modifier: Modifier) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        items(items = updateGuesses) { guess ->
            GuessBox(guess)
        }
    }
}

@Composable
private fun GuessBox(validGuess: String) {
    ConstraintLayout {
        val (text, box) = createRefs()

        Text(
            text = validGuess,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.constrainAs(text) { centerTo(parent) }
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.primary.copy(0.10f))
                .padding(20.dp)
                .constrainAs(box) { centerTo(parent) }
        )
    }
}
