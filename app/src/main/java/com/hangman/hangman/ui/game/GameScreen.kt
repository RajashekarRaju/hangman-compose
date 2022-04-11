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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            val openGameInstructionsDialog = rememberSaveable { mutableStateOf(false) }

            GameScreenContent(
                viewModel = viewModel,
                modalSheetState = modalSheetState,
                openGameInstructionsDialog = openGameInstructionsDialog
            )

            if (viewModel.revealGuessingWord) {
                ShowPopupWhenGameLost(viewModel, navigateUp)
            }

            if (viewModel.gameOverByWinning) {
                ShowDialogWhenGameWon(viewModel, navigateUp)
            }

            if (openGameInstructionsDialog.value) {
                GameInstructionsInfoDialog(
                    viewModel.gameDifficulty,
                    openGameInstructionsDialog
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun GameScreenContent(
    viewModel: GameViewModel,
    modalSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    openGameInstructionsDialog: MutableState<Boolean>
) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (
            navigateBackIconButton, instructionsIconButton, gameProgressInfo, randomWordText,
            alphabetsGridItems
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
                    top.linkTo(parent.top, 20.dp)
                }
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Close game icon",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.alpha(0.75f)
            )
        }

        IconButton(
            onClick = {
                openGameInstructionsDialog.value = !openGameInstructionsDialog.value
            },
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.primary.copy(0.06f),
                    shape = CircleShape
                )
                .constrainAs(instructionsIconButton) {
                    end.linkTo(parent.end, 20.dp)
                    top.linkTo(parent.top, 20.dp)
                }
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Game instructions",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.alpha(0.75f)
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.constrainAs(gameProgressInfo) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, 40.dp)
            }
        ) {
            AttemptsLeftAndLevelProgressBars(viewModel)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AttemptsLeftAndLevelText(viewModel)
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.constrainAs(randomWordText) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(gameProgressInfo.bottom, 16.dp)
                bottom.linkTo(alphabetsGridItems.top, 16.dp)
            }
        ) {
            items(
                items = viewModel.updateGuessesByPlayer.updateGuess
            ) { validGuess ->
                ItemGuessingAlphabetContainer(validGuess)
            }
        }

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
            ApplyAnimatedVisibility(
                densityValue = 400.dp,
                content = {
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
            )
        }
    }
}

@Composable
private fun AttemptsLeftAndLevelText(
    viewModel: GameViewModel
) {
    var incrementLevelBy = 0
    val lastGameLevel = viewModel.maxLevelReached
    if (viewModel.currentPlayerLevel < lastGameLevel) {
        incrementLevelBy = 1
    }

    Text(
        text = buildAnnotatedString {
            append("Level\n")
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colors.primary,
                    fontSize = 28.sp
                )
            ) {
                append(
                    "${viewModel.currentPlayerLevel + incrementLevelBy}/$lastGameLevel"
                )
            }
        },
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.primary.copy(0.50f),
        textAlign = TextAlign.Center,
    )

    Divider(
        modifier = Modifier
            .width(width = 100.dp)
            .padding(vertical = 8.dp)
            .clip(MaterialTheme.shapes.small),
        color = MaterialTheme.colors.primary.copy(0.25f),
        thickness = 2.dp
    )

    Text(
        text = buildAnnotatedString {
            append("Points\n")
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colors.primary,
                    fontSize = 28.sp
                )
            ) {
                append(viewModel.pointsScoredOverall.toString())
            }
        },
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.primary.copy(0.50f),
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun AttemptsLeftAndLevelProgressBars(
    viewModel: GameViewModel
) {
    CreateCircularProgressIndicator(
        currentProgress = animateCurrentLevelProgress(viewModel.currentPlayerLevel),
        indicatorSize = 200.dp
    )

    CreateCircularProgressIndicator(
        currentProgress = 1f,
        progressColor = MaterialTheme.colors.primary.copy(0.25f),
        indicatorSize = 200.dp
    )

    CreateCircularProgressIndicator(
        currentProgress = animateAttemptsLeftProgress(viewModel.attemptsLeftToGuess),
        strokeWidth = 10.dp,
        indicatorSize = 240.dp,
        progressColor = Color.Red.copy(0.95f)
    )

    CreateCircularProgressIndicator(
        currentProgress = 1f,
        strokeWidth = 10.dp,
        progressColor = Color.Green.copy(0.25f),
        indicatorSize = 240.dp
    )
}

@Composable
private fun ItemGuessingAlphabetContainer(
    validGuess: Char
) {
    ConstraintLayout {

        val (alphabet, box) = createRefs()

        Text(
            text = validGuess.toString(),
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

@Composable
private fun ItemAlphabetText(
    alphabet: Alphabets,
    viewModel: GameViewModel
) {
    ConstraintLayout(
        modifier = Modifier
            .alpha(if (!alphabet.isAlphabetGuessed) 1f else 0.25f)
            .clip(CircleShape)
            .size(40.dp)
            .background(color = MaterialTheme.colors.primary.copy(0.12f))
            .clickable(
                enabled = !alphabet.isAlphabetGuessed,
                onClick = {
                    if (!viewModel.gameOverByNoAttemptsLeft) {
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

@Composable
private fun CreateCircularProgressIndicator(
    currentProgress: Float,
    strokeWidth: Dp = 8.dp,
    progressColor: Color = MaterialTheme.colors.primary,
    indicatorSize: Dp
) {
    CircularProgressIndicator(
        progress = currentProgress,
        strokeWidth = strokeWidth,
        color = progressColor,
        modifier = Modifier.size(size = indicatorSize)
    )
}
