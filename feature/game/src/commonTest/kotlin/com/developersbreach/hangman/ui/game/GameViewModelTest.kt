package com.developersbreach.hangman.ui.game

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.HintError
import com.developersbreach.game.core.HintType
import com.developersbreach.hangman.audio.GameSoundEffect
import com.developersbreach.hangman.audio.GameSoundEffectPlayer
import com.developersbreach.hangman.repository.GameSessionRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.Dispatchers
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `back press shows exit dialog and dismiss hides it`() = runTest(dispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()

        viewModel.onEvent(GameEvent.BackPressed)
        assertTrue(viewModel.uiState.value.showExitDialog)

        viewModel.onEvent(GameEvent.ExitDismissed)
        assertFalse(viewModel.uiState.value.showExitDialog)
    }

    @Test
    fun `exit confirmed emits navigate up effect`() = runTest(dispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()

        val effectDeferred = async { viewModel.effects.first() }
        viewModel.onEvent(GameEvent.ExitConfirmed)
        runCurrent()

        assertEquals(GameEffect.NavigateUp, effectDeferred.await())
    }

    @Test
    fun `toggle instructions flips visibility`() = runTest(dispatcher) {
        val viewModel = createViewModel()
        advanceUntilIdle()

        assertFalse(viewModel.uiState.value.showInstructionsDialog)
        viewModel.onEvent(GameEvent.ToggleInstructionsDialog)
        assertTrue(viewModel.uiState.value.showInstructionsDialog)
        viewModel.onEvent(GameEvent.ToggleInstructionsDialog)
        assertFalse(viewModel.uiState.value.showInstructionsDialog)
    }

    @Test
    fun `hint selection consumes hint and starts cooldown`() = runTest(dispatcher) {
        val viewModel = createViewModel(difficulty = GameDifficulty.MEDIUM)
        advanceUntilIdle()

        val hintsBefore = viewModel.uiState.value.hintsRemaining
        viewModel.onEvent(GameEvent.HintSelected(HintType.REVEAL_LETTER))
        runCurrent()

        assertTrue(viewModel.uiState.value.isHintOnCooldown)
        assertEquals(hintsBefore - 1, viewModel.uiState.value.hintsRemaining)

        advanceTimeBy(2_000)
        runCurrent()
        assertFalse(viewModel.uiState.value.isHintOnCooldown)
    }

    @Test
    fun `selecting hint with no hints remaining shows feedback`() = runTest(dispatcher) {
        val viewModel = createViewModel(difficulty = GameDifficulty.MEDIUM)
        advanceUntilIdle()

        viewModel.onEvent(GameEvent.HintSelected(HintType.REVEAL_LETTER))
        runCurrent()
        advanceTimeBy(2_000)
        runCurrent()

        viewModel.onEvent(GameEvent.HintSelected(HintType.REVEAL_LETTER))
        runCurrent()

        val feedback = viewModel.uiState.value.hintFeedback
        assertTrue(viewModel.uiState.value.showHintFeedbackDialog)
        assertNotNull(feedback)
        assertEquals(HintError.NO_HINTS_REMAINING, feedback.error)
    }

    @Test
    fun `timer expiry reveals word and saves history as loss`() = runTest(dispatcher) {
        val sessionRepository = FakeGameSessionRepository()
        val soundPlayer = FakeSoundEffectPlayer()
        val viewModel = createViewModel(
            difficulty = GameDifficulty.HARD,
            sessionRepository = sessionRepository,
            soundPlayer = soundPlayer,
        )
        advanceUntilIdle()

        advanceTimeBy(60_000)
        runCurrent()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.revealGuessingWord)
        assertEquals(0, state.attemptsLeftToGuess)
        assertEquals(1, sessionRepository.savedRequests.size)
        assertTrue(soundPlayer.playedEffects.contains(GameSoundEffect.GAME_LOST))
    }

    private fun createViewModel(
        difficulty: GameDifficulty = GameDifficulty.EASY,
        category: GameCategory = GameCategory.COUNTRIES,
        sessionRepository: FakeGameSessionRepository = FakeGameSessionRepository(),
        soundPlayer: FakeSoundEffectPlayer = FakeSoundEffectPlayer(),
    ): GameViewModel {
        return GameViewModel(
            settingsRepository = FakeGameSettingsRepository(difficulty, category),
            sessionRepository = sessionRepository,
            soundEffectPlayer = soundPlayer,
        )
    }
}

private class FakeGameSettingsRepository(
    private var difficulty: GameDifficulty,
    private var category: GameCategory,
) : GameSettingsRepository {

    override suspend fun getGameDifficulty(): GameDifficulty = difficulty

    override suspend fun getGameCategory(): GameCategory = category

    override suspend fun setGameDifficulty(gameDifficulty: GameDifficulty) {
        difficulty = gameDifficulty
    }

    override suspend fun setGameCategory(gameCategory: GameCategory) {
        category = gameCategory
    }
}

private class FakeGameSessionRepository : GameSessionRepository {
    val savedRequests = mutableListOf<GameHistoryWriteRequest>()

    override suspend fun saveCompletedGame(request: GameHistoryWriteRequest) {
        savedRequests += request
    }
}

private class FakeSoundEffectPlayer : GameSoundEffectPlayer {
    val playedEffects = mutableListOf<GameSoundEffect>()

    override fun play(soundEffect: GameSoundEffect) {
        playedEffects += soundEffect
    }
}
