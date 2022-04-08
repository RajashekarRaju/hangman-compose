package com.hangman.hangman.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
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

    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    val coroutineScope = rememberCoroutineScope()
    BackHandler(enabled = true) {
        coroutineScope.launch {
            modalSheetState.show()
        }
    }

    Surface(
        color = MaterialTheme.colors.background
    ) {
        ModalBottomSheetLayout(
            sheetState = modalSheetState,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetBackgroundColor = MaterialTheme.colors.background,
            scrimColor = Color.Black.copy(0.90f),
            sheetContent = {
                ShowExitGameModalSheet(navigateUp, modalSheetState)
            },
        ) {
            GameScreenContent(
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun GameScreenContent(
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
            alphabetsGridItems, attemptsLeftText, attemptsHintText, gameDifficultyText
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
            text = "Difficulty : ${viewModel.gameDifficulty}",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(gameDifficultyText) {
                centerHorizontallyTo(parent)
                top.linkTo(currentLevelText.bottom, 16.dp)
            }
        )

        Text(
            text = randomGuessingWord,
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(randomWordText) {
                centerHorizontallyTo(parent)
                top.linkTo(gameDifficultyText.bottom)
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

        val playerWon = viewModel.playerWonTheGame

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(alphabetsGridItems) {
                    centerHorizontallyTo(parent)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ) {

            if (playerWon) {

                Text(
                    text = "Moving Next Level",
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(alignment = Alignment.TopCenter),
                    textAlign = TextAlign.Center,
                )

            } else {

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(40.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        items = alphabets,
                        key = { it.alphabetId }
                    ) { alphabet ->
                        ItemAlphabetText(alphabet, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemAlphabetText(
    alphabet: Alphabets,
    viewModel: GameViewModel
) {
    val showAlphabet = remember { mutableStateOf(true) }
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
