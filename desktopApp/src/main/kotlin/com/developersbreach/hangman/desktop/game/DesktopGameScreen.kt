package com.developersbreach.hangman.desktop.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.developersbreach.hangman.viewmodel.GameViewModel

@Composable
fun DesktopGameScreen(viewModel: GameViewModel) {
    val alphabets by viewModel.alphabetsList.collectAsState(initial = emptyList())
    val guesses: SnapshotStateList<String> = viewModel.updatePlayerGuesses

    Row(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        GameProgressInfo(
            modifier = Modifier.align(Alignment.CenterVertically),
            currentPlayerLevel = viewModel.currentPlayerLevel,
            attemptsLeft = viewModel.attemptsLeftToGuess,
            pointsScored = viewModel.pointsScoredOverall,
            maxLevel = viewModel.maxLevelReached
        )

        Column(
            modifier = Modifier.fillMaxHeight().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            GuessedWordRow(updateGuesses = guesses, modifier = Modifier)
            AlphabetsGrid(
                alphabets = alphabets,
                onAlphabetClick = { viewModel.checkIfLetterMatches(it) },
                modifier = Modifier.fillMaxSize().weight(1f)
            )
        }
    }
}
