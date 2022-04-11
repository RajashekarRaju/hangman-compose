package com.hangman.hangman.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .background(MaterialTheme.colors.surface, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 28.dp)
        ) {
            Text(
                text = "Instructions",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
            )

            Spacer(modifier = Modifier.height(20.dp))

            Divider(
                color = MaterialTheme.colors.onSurface.copy(0.50f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 40.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Difficulty : ${gameDifficulty.name}",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Guess the name of the countries.",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary.copy(0.75f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider(
                color = MaterialTheme.colors.onSurface.copy(0.25f),
                thickness = 1.dp,
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
                    .background(MaterialTheme.colors.surface, RoundedCornerShape(16.dp))
            ) {
                items(
                    instructionsList
                ) { item ->
                    Text(
                        text = item,
                        style = MaterialTheme.typography.h6,
                        color = MaterialTheme.colors.onSurface.copy(0.60f),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

val instructionsList = listOf(
    "1. Game has 5 levels, you will win only if you complete all of those.",
    "2. Points per level will be allotted based on bigger the word is you guess.",
    "3. You will be having 8 guessed by default for each level, will reset everytime.",
    "4. For each wrong letter you guess, a attempt will be reduced from 8 guesses.",
    "5. For each correct letter you guess, attempt will not be reduced.",
    "6. You can check all your previous game scoring history in history screen.",
    "7. You have 3 types of difficulties to play the game such as easy, medium, hard. You"
)