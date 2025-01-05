package com.developersbreach.hangman.ui.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.developersbreach.hangman.R
import com.developersbreach.hangman.modal.Alphabets
import com.developersbreach.hangman.ui.components.AppIconButton
import com.developersbreach.hangman.ui.components.ShowExitGameModalSheet
import com.developersbreach.hangman.ui.theme.RedHangmanTheme
import com.developersbreach.hangman.utils.alphabetsList
import kotlinx.coroutines.launch

@Composable
fun GameScreenUI(
    navigateUp: () -> Unit,
    modalSheetState: ModalBottomSheetState,
    openGameInstructionsDialog: MutableState<Boolean>,
    currentPlayerLevel: Int,
    attemptsLeftToGuess: Int,
    pointsScoredOverall: Int,
    maxLevelReached: Int,
    alphabetsList: List<Alphabets>,
    updateGuessesByPlayer: SnapshotStateList<String>,
    checkIfLetterMatches: (alphabet: Alphabets) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

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
            }
        ) {
            // Main content for this screen.
            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (
                    navigateBackIconButton, instructionsIconButton, gameProgressInfo,
                    randomWordText, alphabetsGridItems
                ) = createRefs()

                AppIconButton(
                    onClickIcon = { coroutineScope.launch { modalSheetState.show() } },
                    icon = Icons.Default.Clear,
                    contentDescription = R.string.cd_close_game_icon,
                    modifier = Modifier.constrainAs(navigateBackIconButton) {
                        start.linkTo(parent.start, 20.dp)
                        top.linkTo(parent.top, 20.dp)
                    }
                )

                AppIconButton(
                    onClickIcon = {
                        openGameInstructionsDialog.value = !openGameInstructionsDialog.value
                    },
                    icon = Icons.Outlined.Info,
                    contentDescription = R.string.cd_open_instructions_dialog,
                    modifier = Modifier.constrainAs(instructionsIconButton) {
                        end.linkTo(parent.end, 20.dp)
                        top.linkTo(parent.top, 20.dp)
                    }
                )

                // Contains progress bars, points text information, game level information.
                LevelPointsAttemptsInformation(
                    currentPlayerLevel = currentPlayerLevel,
                    attemptsLeftToGuess = attemptsLeftToGuess,
                    pointsScoredOverall = pointsScoredOverall,
                    maxLevelReached = maxLevelReached,
                    modifier = Modifier.constrainAs(gameProgressInfo) {
                        centerHorizontallyTo(parent)
                        top.linkTo(parent.top, 40.dp)
                    }
                )

                GuessedAlphabetsContainer(
                    updateGuessesByPlayer = updateGuessesByPlayer,
                    modifier = Modifier.constrainAs(randomWordText) {
                        centerHorizontallyTo(parent)
                        top.linkTo(gameProgressInfo.bottom, 16.dp)
                        bottom.linkTo(alphabetsGridItems.top, 16.dp)
                    }
                )

                AlphabetsList(
                    alphabetsList = alphabetsList,
                    checkIfLetterMatches = checkIfLetterMatches,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(alphabetsGridItems) {
                            centerHorizontallyTo(parent)
                            bottom.linkTo(parent.bottom)
                        }
                )
            }
        }
    }
}

@Preview
@Composable
private fun GameScreenUIContent() {
    RedHangmanTheme {
        GameScreenUI(
            navigateUp = { },
            modalSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
            openGameInstructionsDialog = remember { mutableStateOf(false) },
            currentPlayerLevel = 2,
            attemptsLeftToGuess = 5,
            pointsScoredOverall = 4,
            maxLevelReached = 1,
            alphabetsList = alphabetsList(),
            updateGuessesByPlayer = SnapshotStateList(),
            checkIfLetterMatches = { }
        )
    }
}