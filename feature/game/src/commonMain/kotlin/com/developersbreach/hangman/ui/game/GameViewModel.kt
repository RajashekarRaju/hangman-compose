package com.developersbreach.hangman.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.game.core.GameSessionEngine
import com.developersbreach.game.core.GameSessionState
import com.developersbreach.hangman.audio.GameSoundEffect
import com.developersbreach.hangman.audio.GameSoundEffectPlayer
import com.developersbreach.hangman.repository.GameSessionRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val settingsRepository: GameSettingsRepository,
    private val sessionRepository: GameSessionRepository,
    private val soundEffectPlayer: GameSoundEffectPlayer,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<GameEffect>()
    val effects: SharedFlow<GameEffect> = _effects.asSharedFlow()

    private var gameSessionEngine: GameSessionEngine? = null

    init {
        hydrateGameSession()
        soundEffectPlayer.play(GameSoundEffect.LEVEL_WON)
    }

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.AlphabetClicked -> checkIfLetterMatches(event.alphabetId)
            GameEvent.BackPressed -> {
                _uiState.update { current ->
                    current.copy(showExitDialog = true)
                }
            }
            GameEvent.ExitDismissed -> {
                _uiState.update { current ->
                    current.copy(showExitDialog = false)
                }
            }
            GameEvent.ToggleInstructionsDialog -> {
                _uiState.update { current ->
                    current.copy(showInstructionsDialog = !current.showInstructionsDialog)
                }
            }
            GameEvent.ExitConfirmed,
            GameEvent.WinDialogDismissed,
            GameEvent.LostDialogDismissed -> emitEffect(GameEffect.NavigateUp)
        }
    }

    private fun hydrateGameSession() {
        viewModelScope.launch {
            val gameDifficulty = settingsRepository.getGameDifficulty()
            val gameCategory = settingsRepository.getGameCategory()
            val guessingWordsForCurrentGame =
                sessionRepository.getRandomGuessingWord(gameDifficulty, gameCategory)

            gameSessionEngine = GameSessionEngine(
                guessingWordsForCurrentGame = guessingWordsForCurrentGame,
                maxAttempts = 8,
                levelsPerGame = _uiState.value.maxLevelReached,
            )

            _uiState.update { current ->
                current.copy(
                    gameDifficulty = gameDifficulty,
                    gameCategory = gameCategory,
                )
            }

            syncState(gameSessionEngine!!.snapshot())
        }
    }

    private fun checkIfLetterMatches(alphabetId: Int) {
        val engine = gameSessionEngine ?: return
        val update = engine.guessAlphabet(alphabetId)
        syncState(update.state)

        if (update.levelCompleted && !update.gameWon) {
            soundEffectPlayer.play(GameSoundEffect.LEVEL_WON)
        }

        if (update.gameWon) {
            soundEffectPlayer.play(GameSoundEffect.GAME_WON)
            viewModelScope.launch {
                delay(500)
                saveCurrentGameToHistory()
            }
        }

        if (update.gameLost) {
            viewModelScope.launch {
                soundEffectPlayer.play(GameSoundEffect.GAME_LOST)
                saveCurrentGameToHistory()
            }
        }

        soundEffectPlayer.play(GameSoundEffect.ALPHABET_TAP)
    }

    private fun syncState(state: GameSessionState) {
        _uiState.update { current ->
            current.copy(
                alphabetsList = state.alphabets,
                playerGuesses = state.playerGuesses,
                wordToGuess = state.currentWord,
                attemptsLeftToGuess = state.attemptsLeftToGuess,
                currentPlayerLevel = state.currentPlayerLevel,
                pointsScoredOverall = state.pointsScoredOverall,
                gameOverByWinning = state.gameOverByWinning,
                revealGuessingWord = state.gameOverByNoAttemptsLeft,
                showInstructionsDialog = if (state.gameOverByWinning || state.gameOverByNoAttemptsLeft) {
                    false
                } else {
                    current.showInstructionsDialog
                },
                showExitDialog = if (state.gameOverByWinning || state.gameOverByNoAttemptsLeft) {
                    false
                } else {
                    current.showExitDialog
                },
            )
        }
    }

    private suspend fun saveCurrentGameToHistory() {
        val state = _uiState.value
        sessionRepository.saveCompletedGame(
            GameHistoryWriteRequest(
                gameScore = state.pointsScoredOverall,
                gameLevel = state.currentPlayerLevel,
                gameSummary = state.gameOverByWinning,
                gameDifficulty = state.gameDifficulty,
                gameCategory = state.gameCategory,
            ),
        )
    }

    private fun emitEffect(effect: GameEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }
}