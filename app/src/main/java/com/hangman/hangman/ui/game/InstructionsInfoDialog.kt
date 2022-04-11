package com.hangman.hangman.ui.game

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.hangman.hangman.utils.GameDifficulty


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameInstructionsInfoDialog(
    gameDifficulty: GameDifficulty,
    openGameInstructionsDialog: MutableState<Boolean>
) {
    Dialog(
        onDismissRequest = {
            openGameInstructionsDialog.value = !openGameInstructionsDialog.value
        }
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.surface, RoundedCornerShape(16.dp))
                .padding(horizontal = 20.dp, vertical = 28.dp)
        ) {
            stickyHeader {
                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface,
                )

                Spacer(modifier = Modifier.height(20.dp))

                Divider(
                    color = MaterialTheme.colors.onSurface.copy(0.50f),
                    thickness = 1.dp,
                )
            }

            item {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Difficulty : ${gameDifficulty.name}",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Add instructions here

                Text(
                    text = "Guess the name of the countries.",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary.copy(0.75f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

