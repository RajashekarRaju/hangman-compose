package com.developersbreach.hangman.ui.game

import androidx.activity.compose.BackHandler
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import com.developersbreach.hangman.ui.components.GameInstructionsInfoDialog
import com.developersbreach.hangman.ui.components.ShowDialogWhenGameWon
import com.developersbreach.hangman.ui.components.ShowPopupWhenGameLost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Game screen, can be navigated from onboarding screen.
 * This screen has it's own ViewModel [GameViewModel]
 */
@Composable
fun GameScreen(
    navigateUp: () -> Unit,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    viewModel: GameViewModel
) {
    val alphabetsList by viewModel.alphabetsList.observeAsState(listOf())
    val updateGuessesByPlayer = viewModel.updatePlayerGuesses

    // State for showing game instructions dialog.
    val openGameInstructionsDialog = rememberSaveable { mutableStateOf(false) }

    // If true, a dialog will show up with player lost message.
    val shouldRevealWord by viewModel.revealGuessingWord.observeAsState(false)

    // Modal sheet to ask player whether to quit playing the game, triggered while up navigation.
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    // Take control of back button so that user cannot navigate back from the game in middle.
    // First show the modal sheet and confirm to exit.
    BackHandler(enabled = true) {
        coroutineScope.launch {
            modalSheetState.show()
        }
    }

    if (shouldRevealWord) {
        ShowPopupWhenGameLost(
            navigateUp = navigateUp,
            wordToGuess = viewModel.wordToGuess
        )
    }

    // If true, a dialog will show up with player won message.
    if (viewModel.gameOverByWinning) {
        ShowDialogWhenGameWon(
            navigateUp = navigateUp,
            pointsScoredOverall = viewModel.pointsScoredOverall,
            gameDifficulty = viewModel.gameDifficulty
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

    GameScreenUI(
        navigateUp = navigateUp,
        modalSheetState = modalSheetState,
        openGameInstructionsDialog = openGameInstructionsDialog,
        currentPlayerLevel = viewModel.currentPlayerLevel,
        attemptsLeftToGuess = viewModel.attemptsLeftToGuess,
        pointsScoredOverall = viewModel.pointsScoredOverall,
        maxLevelReached = viewModel.maxLevelReached,
        alphabetsList = alphabetsList,
        updateGuessesByPlayer = updateGuessesByPlayer,
        gameOverByNoAttemptsLeft = viewModel.gameOverByNoAttemptsLeft,
        checkIfLetterMatches = {
            viewModel.checkIfLetterMatches(it)
        }
    )
}