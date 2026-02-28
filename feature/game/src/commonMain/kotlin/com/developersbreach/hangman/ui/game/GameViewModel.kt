package com.developersbreach.hangman.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developersbreach.game.core.achievements.AchievementCatalog
import com.developersbreach.game.core.GameSessionEngine
import com.developersbreach.game.core.GameSessionUpdate
import com.developersbreach.game.core.GameSessionState
import com.developersbreach.game.core.HintType
import com.developersbreach.game.core.MAX_ATTEMPTS_PER_LEVEL
import com.developersbreach.game.core.getFilteredWordsByGameDifficulty
import com.developersbreach.game.core.hintsPerLevelForDifficulty
import com.developersbreach.game.core.achievements.initialProgress
import com.developersbreach.hangman.audio.GameSoundEffect
import com.developersbreach.hangman.audio.GameSoundEffectPlayer
import com.developersbreach.hangman.repository.AchievementsRepository
import com.developersbreach.hangman.repository.GameSessionRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest
import com.developersbreach.hangman.ui.common.notification.AchievementNotificationCoordinator
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet

class GameViewModel(
    private val settingsRepository: GameSettingsRepository,
    private val sessionRepository: GameSessionRepository,
    private val achievementsRepository: AchievementsRepository,
    private val soundEffectPlayer: GameSoundEffectPlayer,
    private val achievementNotificationCoordinator: AchievementNotificationCoordinator,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<GameEffect>()
    val effects: SharedFlow<GameEffect> = _effects.asSharedFlow()

    private var gameSessionEngine: GameSessionEngine? = null
    private var levelTimerJob: Job? = null
    private var hintCooldownJob: Job? = null
    private var hintFeedbackDismissJob: Job? = null
    private val achievementTracker = GameAchievementTracker(nowMillis = ::clockNowMillis)

    init {
        hydrateGameSession()
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
            is GameEvent.HintSelected -> applyHint(event.hintType)
            GameEvent.DismissHintFeedbackDialog -> {
                hintFeedbackDismissJob?.cancel()
                _uiState.update { current ->
                    current.copy(
                        showHintFeedbackDialog = false,
                        hintFeedback = null,
                    )
                }
            }
            GameEvent.ExitConfirmed,
            GameEvent.WinDialogDismissed,
            GameEvent.LostDialogDismissed -> emitEffect(GameEffect.NavigateUp)
        }
    }

    private fun hydrateGameSession() {
        viewModelScope.launch {
            val progress = achievementsRepository.observeAchievementProgress().firstOrNull()
                ?.takeIf { existing -> existing.isNotEmpty() }
                ?: AchievementCatalog.definitions.map { it.initialProgress() }
            val counters = achievementsRepository.observeAchievementStatCounters().first()
            achievementTracker.hydrate(
                initialProgress = progress,
                initialCounters = counters,
            )

            val gameDifficulty = settingsRepository.getGameDifficulty()
            val gameCategory = settingsRepository.getGameCategory()
            val guessingWordsForCurrentGame =
                getFilteredWordsByGameDifficulty(gameDifficulty, gameCategory)

            gameSessionEngine = GameSessionEngine(
                guessingWordsForCurrentGame = guessingWordsForCurrentGame,
                maxAttempts = MAX_ATTEMPTS_PER_LEVEL,
                levelsPerGame = _uiState.value.maxLevelReached,
                hintsPerLevel = hintsPerLevelForDifficulty(gameDifficulty),
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

    private fun applyHint(hintType: HintType) {
        if (_uiState.value.isHintOnCooldown) return
        val engine = gameSessionEngine ?: return
        val update = engine.applyHint(hintType)
        processSessionUpdate(update = update, playTapSound = false)

        if (update.hintApplied) {
            soundEffectPlayer.play(GameSoundEffect.ALPHABET_TAP)
            startHintCooldown()
        }

        val hintError = update.hintError
        if (!update.hintApplied && hintError != null) {
            val state = _uiState.value
            logHintUnavailable(
                hintType = hintType,
                error = hintError.name,
                level = state.displayedLevel,
                attemptsLeft = state.attemptsLeftToGuess,
                hintsRemaining = state.hintsRemaining,
                word = state.wordToGuess,
            )
            _uiState.update { current ->
                current.copy(
                    showHintFeedbackDialog = true,
                    hintFeedback = HintFeedback(
                        selectedHintType = hintType,
                        error = hintError,
                    ),
                )
            }
            startHintFeedbackAutoDismiss()
        }
    }

    private fun processSessionUpdate(
        update: GameSessionUpdate,
        playTapSound: Boolean,
    ) {
        val levelTimeRemainingAtUpdate = _uiState.value.levelTimeRemainingMillis
        syncState(update.state)
        evaluateAchievements(
            update = update,
            levelTimeRemainingAtUpdate = levelTimeRemainingAtUpdate,
        )

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
                showInstructionsDialog = when {
                    state.gameOverByWinning || state.gameOverByNoAttemptsLeft -> false
                    else -> current.showInstructionsDialog
                },
                showExitDialog = when {
                    state.gameOverByWinning || state.gameOverByNoAttemptsLeft -> false
                    else -> current.showExitDialog
                },
                hintsRemaining = state.hintsRemaining,
                hintsUsedTotal = state.hintsUsedTotal,
                hintTypesUsed = state.hintTypesUsed,
                categoryHint = buildGameCategoryHintUiModel(
                    wordToGuess = state.currentWord,
                    category = current.gameCategory,
                ),
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
        hintCooldownJob?.cancel()
        val stateAfterTimeout = _uiState.updateAndGet { current ->
            current.copy(
                revealGuessingWord = true,
                showInstructionsDialog = false,
                showExitDialog = false,
                attemptsLeftToGuess = 0,
            )
        }
        evaluateAchievements(
            update = GameSessionUpdate(
                state = stateAfterTimeout.toSessionState(),
                levelCompleted = false,
                gameWon = false,
                gameLost = true,
            ),
            levelTimeRemainingAtUpdate = 0L,
        )
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
                hintsUsed = state.hintsUsedTotal,
                hintTypesUsed = state.hintTypesUsed.toList(),
            ),
        )
        onHistoryEntryRecorded()
    }

    private suspend fun onHistoryEntryRecorded() {
        val result = achievementTracker.onHistoryRecorded()
        achievementsRepository.saveAchievementStatCounters(result.updatedCounters)
        achievementsRepository.replaceAchievementProgress(result.updatedProgress)

        if (result.newlyUnlockedIds.isNotEmpty()) {
            achievementNotificationCoordinator.enqueueUnlocked(result.newlyUnlockedIds)
        }
    }

    private fun evaluateAchievements(
        update: GameSessionUpdate,
        levelTimeRemainingAtUpdate: Long,
    ) {
        val currentState = _uiState.value
        val result = achievementTracker.onGameUpdate(
            update = update,
            levelTimeRemainingAtUpdate = levelTimeRemainingAtUpdate,
            gameCategory = currentState.gameCategory,
            gameDifficulty = currentState.gameDifficulty,
        )

        viewModelScope.launch {
            achievementsRepository.saveAchievementStatCounters(result.updatedCounters)
            achievementsRepository.replaceAchievementProgress(result.updatedProgress)
        }

        if (result.newlyUnlockedIds.isNotEmpty()) {
            achievementNotificationCoordinator.enqueueUnlocked(result.newlyUnlockedIds)
        }
    }

    private fun clockNowMillis(): Long = nowEpochMillis()

    private fun startHintCooldown() {
        hintCooldownJob?.cancel()
        _uiState.update { current ->
            current.copy(isHintOnCooldown = true)
        }
        hintCooldownJob = viewModelScope.launch {
            delay(HINT_COOLDOWN_MILLIS)
            _uiState.update { current ->
                current.copy(isHintOnCooldown = false)
            }
        }
    }

    private fun emitEffect(effect: GameEffect) {
        viewModelScope.launch {
            _effects.emit(effect)
        }
    }

    private fun startHintFeedbackAutoDismiss() {
        hintFeedbackDismissJob?.cancel()
        hintFeedbackDismissJob = viewModelScope.launch {
            delay(HINT_FEEDBACK_DISMISS_MILLIS)
            _uiState.update { current ->
                current.copy(
                    showHintFeedbackDialog = false,
                    hintFeedback = null,
                )
            }
        }
    }

    private fun logHintUnavailable(
        hintType: HintType,
        error: String,
        level: Int,
        attemptsLeft: Int,
        hintsRemaining: Int,
        word: String,
    ) {
        println(
            "HintUnavailable type=$hintType error=$error level=$level attemptsLeft=$attemptsLeft " +
                "hintsRemaining=$hintsRemaining word=\"$word\""
        )
    }

    override fun onCleared() {
        levelTimerJob?.cancel()
        hintCooldownJob?.cancel()
        hintFeedbackDismissJob?.cancel()
        super.onCleared()
    }

    companion object {
        private const val TIMER_TICK_MILLIS = 100L
        private const val LEVEL_TIMER_TOTAL_MILLIS = 60_000L
        private const val HINT_COOLDOWN_MILLIS = 2_000L
        private const val HINT_FEEDBACK_DISMISS_MILLIS = 2_000L
    }
}