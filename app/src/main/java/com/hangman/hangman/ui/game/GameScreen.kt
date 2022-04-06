package com.hangman.hangman.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hangman.hangman.HangmanApp
import com.hangman.hangman.modal.Alphabets
import com.hangman.hangman.repository.GameRepository


@Composable
fun GameScreen(
    navigateUp: () -> Unit,
    application: HangmanApp,
    repository: GameRepository
) {
    val viewModel = viewModel(
        factory = GameViewModel.provideFactory(application, repository),
        modelClass = GameViewModel::class.java
    )

    Surface(
        color = MaterialTheme.colors.background
    ) {
        GameScreenContent(
            navigateUp = navigateUp,
            viewModel = viewModel
        )
    }
}

@Composable
private fun GameScreenContent(
    navigateUp: () -> Unit,
    viewModel: GameViewModel
) {
    val randomGuessingWord = viewModel.guessingWord
    val alphabets = viewModel.alphabets
    val attemptsCount = viewModel.attemptsLeft
    val pointsScored = viewModel.pointsScoredPerWord
    val currentLevel = viewModel.currentPlayerLevel

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (
            pointsScoredText, currentLevelText, randomWordText,
            alphabetsGridItems, attemptsLeftText, attemptsHintText
        ) = createRefs()

        Text(
            text = "Points : $pointsScored",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(pointsScoredText) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, 24.dp)
            }
        )

        Text(
            text = "Level : $currentLevel/5",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(currentLevelText) {
                centerHorizontallyTo(parent)
                top.linkTo(pointsScoredText.bottom, 16.dp)
            }
        )

        Text(
            text = randomGuessingWord,
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(randomWordText) {
                centerHorizontallyTo(parent)
                top.linkTo(currentLevelText.bottom)
                bottom.linkTo(attemptsHintText.top)
            }
        )

        Text(
            text = "attempts left",
            style = MaterialTheme.typography.subtitle2,
            color = Color.White.copy(0.50f),
            modifier = Modifier.constrainAs(attemptsHintText) {
                centerHorizontallyTo(parent)
                bottom.linkTo(attemptsLeftText.top, 16.dp)
            }
        )

        Text(
            text = "$attemptsCount/5",
            style = MaterialTheme.typography.h3,
            color = Color.White,
            modifier = Modifier.constrainAs(attemptsLeftText) {
                centerHorizontallyTo(parent)
                bottom.linkTo(alphabetsGridItems.top, 24.dp)
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(40.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(20.dp),
            modifier = Modifier
                .constrainAs(alphabetsGridItems) {
                    centerHorizontallyTo(parent)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            items(
                items = alphabets,
                key = {
                    it.alphabetId
                }
            ) { alphabet ->
                ItemAlphabetText(alphabet, viewModel)
            }
        }
    }
}

@Composable
private fun ItemAlphabetText(
    alphabet: Alphabets,
    viewModel: GameViewModel
) {
    val showAlphabet = rememberSaveable { mutableStateOf(true) }
    val isGameOver = viewModel.gameOver

    ConstraintLayout(
        modifier = Modifier
            .alpha(if (showAlphabet.value) 1f else 0.25f)
            .clip(CircleShape)
            .size(40.dp)
            .background(color = MaterialTheme.colors.primary.copy(0.12f))
            .clickable(
                enabled = showAlphabet.value,
                onClick = {
                    if (!isGameOver) {
                        viewModel.checkIfLetterMatches(alphabet)
                        showAlphabet.value = !showAlphabet.value
                    }
                }
            )
    ) {
        val (alphabetText) = createRefs()

        Text(
            text = alphabet.alphabet,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.constrainAs(alphabetText) {
                centerTo(parent)
            }
        )
    }
}
