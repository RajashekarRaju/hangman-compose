package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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

/**
 * Places guessed alphabets in box.
 * Once the level is completed, all the letters will be reset to empty.
 */
@Composable
fun GuessedAlphabetsContainer(
    updateGuessesByPlayer: SnapshotStateList<String>,
    modifier: Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        items(
            // List contains current matched guessing words.
            items = updateGuessesByPlayer
        ) { validGuess ->
            ItemGuessingAlphabetContainer(validGuess)
        }
    }
}

@Composable
private fun ItemGuessingAlphabetContainer(
    validGuess: String
) {
    ConstraintLayout {
        val (alphabet, box) = createRefs()

        Text(
            text = validGuess,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.constrainAs(alphabet) {
                centerTo(parent)
            }
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colors.primary.copy(0.10f))
                .padding(20.dp)
                .constrainAs(box) {
                    centerTo(parent)
                }
        )
    }
}