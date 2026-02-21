package com.developersbreach.hangman.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.game.core.GameSessionEngine
import com.developersbreach.game.core.GameSessionState
import com.developersbreach.game.core.getFilteredWordsByGameDifficulty
import com.developersbreach.hangman.audio.GameSoundEffect
import com.developersbreach.hangman.audio.GameSoundEffectPlayer
import com.developersbreach.hangman.repository.GameSessionRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.Job
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
    private var levelTimerJob: Job? = null

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
                getFilteredWordsByGameDifficulty(gameDifficulty, gameCategory)

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
            resetLevelTimer()
        }
    }

    private fun checkIfLetterMatches(alphabetId: Int) {
        val engine = gameSessionEngine ?: return
        val update = engine.guessAlphabet(alphabetId)
        processSessionUpdate(update = update, playTapSound = true)
    }

    private fun processSessionUpdate(
        update: com.developersbreach.game.core.GameSessionUpdate,
        playTapSound: Boolean,
    ) {
        syncState(update.state)

        if (update.levelCompleted && !update.gameWon) {
            soundEffectPlayer.play(GameSoundEffect.LEVEL_WON)
            resetLevelTimer()
        }

        if (update.gameWon) {
            levelTimerJob?.cancel()
            soundEffectPlayer.play(GameSoundEffect.GAME_WON)
            viewModelScope.launch {
                delay(500)
                saveCurrentGameToHistory()
            }
        }

        if (update.gameLost) {
            levelTimerJob?.cancel()
            viewModelScope.launch {
                soundEffectPlayer.play(GameSoundEffect.GAME_LOST)
                saveCurrentGameToHistory()
            }
        }

        if (playTapSound) {
            soundEffectPlayer.play(GameSoundEffect.ALPHABET_TAP)
        }
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

    private fun resetLevelTimer() {
        val totalMillis = LEVEL_TIMER_TOTAL_MILLIS
        _uiState.update { current ->
            current.copy(
                levelTimeTotalMillis = totalMillis,
                levelTimeRemainingMillis = totalMillis,
            )
        }
        startLevelTimerIfNeeded()
    }

    private fun startLevelTimerIfNeeded() {
        levelTimerJob?.cancel()
        levelTimerJob = viewModelScope.launch {
            while (isActive) {
                delay(TIMER_TICK_MILLIS)
                if (shouldPauseTimer()) continue

                val nextRemaining = (_uiState.value.levelTimeRemainingMillis - TIMER_TICK_MILLIS)
                    .coerceAtLeast(0L)
                _uiState.update { current ->
                    current.copy(levelTimeRemainingMillis = nextRemaining)
                }

                if (nextRemaining == 0L) {
                    onTimerExpired()
                    break
                }
            }
        }
    }

    private fun onTimerExpired() {
        levelTimerJob?.cancel()
        _uiState.update { current ->
            current.copy(
                revealGuessingWord = true,
                showInstructionsDialog = false,
                showExitDialog = false,
                attemptsLeftToGuess = 0,
            )
        }
        viewModelScope.launch {
            soundEffectPlayer.play(GameSoundEffect.GAME_LOST)
            saveCurrentGameToHistory()
        }
    }

    private fun shouldPauseTimer(): Boolean {
        val state = _uiState.value
        return state.showExitDialog ||
            state.showInstructionsDialog ||
            state.gameOverByWinning ||
            state.revealGuessingWord
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

    override fun onCleared() {
        levelTimerJob?.cancel()
        super.onCleared()
    }

    companion object {
        private const val TIMER_TICK_MILLIS = 100L
        private const val LEVEL_TIMER_TOTAL_MILLIS = 60_000L
    }
}