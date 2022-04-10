package com.hangman.hangman.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
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
import com.hangman.hangman.utils.ApplyAnimatedVisibility
import kotlinx.coroutines.CoroutineScope
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
                viewModel = viewModel,
                modalSheetState = modalSheetState
            )

            if (viewModel.revealGuessingWord) {
                ShowPopupWhenGameLost(viewModel, navigateUp)
            }

            if (viewModel.gameOverByWinning) {
                ShowDialogWhenGameWon(viewModel, navigateUp)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun GameScreenContent(
    viewModel: GameViewModel,
    modalSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (
            navigateBackIconButton, pointsScoredText, currentLevelText, randomWordText,
            alphabetsGridItems, attemptsLeftText, attemptsHintText, gameDifficultyText
        ) = createRefs()

        IconButton(
            onClick = {
                coroutineScope.launch {
                    modalSheetState.show()
                }
            },
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.primary.copy(0.06f),
                    shape = CircleShape
                )
                .constrainAs(navigateBackIconButton) {
                    start.linkTo(parent.start, 20.dp)
                    top.linkTo(pointsScoredText.top)
                    bottom.linkTo(pointsScoredText.bottom)
                }
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Close game icon",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.alpha(0.75f)
            )
        }

        Text(
            text = "Points : ${viewModel.pointsScoredOverall}",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(pointsScoredText) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, 24.dp)
            }
        )

        Text(
            text = "Level : ${viewModel.currentPlayerLevel + 1}/5",
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

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(randomWordText) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(gameDifficultyText.bottom)
                bottom.linkTo(attemptsHintText.top)
            }
        ) {
            items(
                items = viewModel.updateGuessesByPlayer.updateGuess
            ) { validGuess ->

                ConstraintLayout {

                    val (alphabet, box) = createRefs()

                    Text(
                        text = validGuess.toString(),
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.primary,
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
        }

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
            text = "${viewModel.attemptsLeftToGuess}/8",
            style = MaterialTheme.typography.h3,
            color = Color.White,
            modifier = Modifier.constrainAs(attemptsLeftText) {
                centerHorizontallyTo(parent)
                bottom.linkTo(alphabetsGridItems.top, 24.dp)
            }
        )

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

            ApplyAnimatedVisibility {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(40.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        items = viewModel.alphabets,
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
    val isGameOver = viewModel.gameOverByNoAttemptsLeft

    ConstraintLayout(
        modifier = Modifier
            .alpha(if (!alphabet.isAlphabetGuessed) 1f else 0.25f)
            .clip(CircleShape)
            .size(40.dp)
            .background(color = MaterialTheme.colors.primary.copy(0.12f))
            .clickable(
                enabled = !alphabet.isAlphabetGuessed,
                onClick = {
                    if (!isGameOver) {
                        viewModel.checkIfLetterMatches(alphabet)
                        alphabet.isAlphabetGuessed = true
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
