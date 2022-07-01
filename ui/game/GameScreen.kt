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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.hangman.hangman.R
import com.hangman.hangman.modal.Alphabets
import com.hangman.hangman.utils.ApplyAnimatedVisibility
import com.hangman.hangman.utils.SparkAnimateGuessedLetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Game screen, can be navigated from onboarding screen.
 * This screen has it's own ViewModel [GameViewModel]
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GameScreen(
    navigateUp: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    viewModel: GameViewModel
) {
    // Modal sheet to ask player whether to quit playing the game, triggered while up navigation.
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    // Take control of back button so that user cannot navigate back from the game in middle.
    // First show the modal sheet and confirm to exit.
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
                ShowExitGameModalSheet(
                    navigateUp = navigateUp,
                    modalSheetState = modalSheetState
                )
            },
        ) {
            // State for showing game instructions dialog.
            val openGameInstructionsDialog = rememberSaveable { mutableStateOf(false) }

            // Main content for this screen.
            GameScreenContent(
                viewModel = viewModel,
                modalSheetState = modalSheetState,
                openGameInstructionsDialog = openGameInstructionsDialog
            )

            // If true, a dialog will show up with player lost message.
            val shouldRevealWord by viewModel.revealGuessingWord.collectAsState(false)
            if (shouldRevealWord) {
                ShowPopupWhenGameLost(
                    viewModel = viewModel,
                    navigateUp = navigateUp
                )
            }

            // If true, a dialog will show up with player won message.
            if (viewModel.gameOverByWinning) {
                ShowDialogWhenGameWon(
                    viewModel = viewModel,
                    navigateUp = navigateUp
                )
            }

            // If true, a dialog will show up with game instructions message.
            if (openGameInstructionsDialog.value) {
                GameInstructionsInfoDialog(
                    gameDifficulty = viewModel.gameDifficulty,
                    gameCategory = viewModel.gameCategory,
                    openGameInstructionsDialog = openGameInstructionsDialog
                )
            }
        }
    }
}

/**
 * Contains all game screen contents.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun GameScreenContent(
    viewModel: GameViewModel,
    modalSheetState: ModalBottomSheetState,
    openGameInstructionsDialog: MutableState<Boolean>,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
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
                contentDescription = stringResource(R.string.cd_close_game_icon),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.alpha(0.75f)
            )
        }

        IconButton(
            onClick = { openGameInstructionsDialog.value = !openGameInstructionsDialog.value },
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
                contentDescription = stringResource(R.string.cd_open_instructions_dialog),
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.alpha(0.75f)
            )
        }

        // Contains progress bars, points text information, game level information.
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.constrainAs(gameProgressInfo) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, 40.dp)
            }
        ) {
            // Circular animated progress bars for attempts left and level streak.
            AttemptsLeftAndLevelProgressBars(viewModel)

            // Text with points scored, levels completed.
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AttemptsLeftAndLevelText(viewModel)
            }
        }

        val guessesList = viewModel.updateGuessesByPlayer.value.toMutableStateList()

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
            if (guessesList != null) {
                items(
                    // List contains current matched guessing words.
                    items = guessesList
                ) { validGuess ->
                    ItemGuessingAlphabetContainer(validGuess)
                }
            }
        }

        val alphabetsList by viewModel.alphabetsList.collectAsState(initial = listOf())

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
                            items = alphabetsList,
                            // Since all alphabets contains unique id's,
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

/**
 * Item for each alphabet in the alphabets list.
 */
@Composable
private fun ItemAlphabetText(
    alphabet: Alphabets,
    viewModel: GameViewModel
) {
    // If alphabet is correctly guessed,
    // Reduce it's alpha, so that player know it's already used.
    // Also disable the click for that specific alphabet.
    ConstraintLayout(
        modifier = Modifier
            .alpha(if (!alphabet.isAlphabetGuessed) 1f else 0.25f)
            .clip(CircleShape)
            .size(40.dp)
            .background(color = MaterialTheme.colors.primary.copy(0.12f))
            .clickable(
                enabled = !alphabet.isAlphabetGuessed,
                onClick = {
                    // Don't let player click items if game is over.
                    if (!viewModel.gameOverByNoAttemptsLeft) {
                        // For each guess check if match is correct from ViewModel.
                        viewModel.checkIfLetterMatches(alphabet)
                        // Immediately disable click and reduce alpha for this item.
                        alphabet.isAlphabetGuessed = true
                    }
                }
            )
    ) {
        val (alphabetText, clickEffectAnim) = createRefs()

        if (alphabet.isAlphabetGuessed) {
            Box(
                modifier = Modifier.constrainAs(clickEffectAnim) {
                    centerTo(parent)
                }
            ) {
                SparkAnimateGuessedLetter()
            }
        }

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

/**
 * These elements will be inside the circular progress bars.
 * Updates the current player game level to text.
 *
 */
@Composable
private fun AttemptsLeftAndLevelText(
    viewModel: GameViewModel
) {
    // When player completes last level, the level value jumps to +1, to prevent that start level
    // from 0 and make sure to never increment level if max level reached.
    var incrementLevelBy = 0
    val lastGameLevel = viewModel.maxLevelReached
    if (viewModel.currentPlayerLevel < lastGameLevel) {
        incrementLevelBy = 1
    }

    Text(
        text = buildAnnotatedString {
            append(stringResource(id = R.string.current_level_header))
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colors.primary,
                    fontSize = 28.sp
                )
            ) {
                append("${viewModel.currentPlayerLevel + incrementLevelBy}/$lastGameLevel")
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
            append(stringResource(id = R.string.current_points_header))
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
    // Animates and keeps track of current level player is in.
    CreateCircularProgressIndicator(
        currentProgress = animateCurrentLevelProgress(viewModel.currentPlayerLevel),
        indicatorSize = 200.dp
    )

    // Doesn't animate level.
    // Filled with light primary color for player to understand the total levels to win.
    CreateCircularProgressIndicator(
        currentProgress = 1f,
        progressColor = MaterialTheme.colors.primary.copy(0.25f),
        indicatorSize = 200.dp
    )

    // Animates the current attempts completed in red color.
    // For each wrong guess, red progress will be filled.
    CreateCircularProgressIndicator(
        currentProgress = animateAttemptsLeftProgress(viewModel.attemptsLeftToGuess),
        strokeWidth = 10.dp,
        indicatorSize = 240.dp,
        progressColor = Color.Red.copy(0.95f)
    )

    // Doesn't animate attempts.
    // Filled with green color for player to understand how many attempts he is left with.
    CreateCircularProgressIndicator(
        currentProgress = 1f,
        strokeWidth = 10.dp,
        progressColor = Color.Green.copy(0.25f),
        indicatorSize = 240.dp
    )
}

/**
 * Actively places guessed alphabets in box.
 * Once the level is completed, all the letters will be reset to empty.
 */
@Composable
private fun ItemGuessingAlphabetContainer(
    validGuess: String,
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

// Reusable composable for all 4 progress bars.
@Composable
fun CreateCircularProgressIndicator(
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