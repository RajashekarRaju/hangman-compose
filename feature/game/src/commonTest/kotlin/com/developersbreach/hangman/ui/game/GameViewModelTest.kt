package com.developersbreach.hangman.ui.game

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.HintError
import com.developersbreach.game.core.HintType
import com.developersbreach.game.core.MAX_ATTEMPTS_PER_LEVEL
import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.game.core.achievements.AchievementProgress
import com.developersbreach.game.core.achievements.AchievementStatCounters
import com.developersbreach.hangman.audio.GameSoundEffect
import com.developersbreach.hangman.audio.GameSoundEffectPlayer
import com.developersbreach.hangman.repository.AchievementsRepository
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.repository.CursorStyle
import com.developersbreach.hangman.repository.GameProgressVisualPreference
import com.developersbreach.hangman.repository.GameSessionRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest
import com.developersbreach.hangman.ui.common.notification.AchievementBannerUiState
import com.developersbreach.hangman.ui.common.notification.AchievementNotificationCoordinator
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

        assertFalse(viewModel.uiState.value.showGameGuideOverlay)
        viewModel.onEvent(GameEvent.ToggleGameGuideOverlay)
        assertTrue(viewModel.uiState.value.showGameGuideOverlay)
        viewModel.onEvent(GameEvent.ToggleGameGuideOverlay)
        assertFalse(viewModel.uiState.value.showGameGuideOverlay)
    }

    @Test
    fun `hydration maps stored game visual preference into ui state`() = runTest(dispatcher) {
        val viewModel = createViewModel(
            gameProgressVisualPreference = GameProgressVisualPreference.LEVEL_POINTS_ATTEMPTS,
        )
        advanceUntilIdle()

        assertEquals(
            GameProgressVisualType.LevelPointsAttemptsInformation,
            viewModel.uiState.value.progressVisualType,
        )
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

        advanceTimeBy(2_000)
        runCurrent()
        assertFalse(viewModel.uiState.value.showHintFeedbackDialog)
        assertEquals(null, viewModel.uiState.value.hintFeedback)
    }

    @Test
    fun `timer expiry reveals word and saves history as loss`() = runTest(dispatcher) {
        val sessionRepository = FakeGameSessionRepository()
        val soundPlayer = FakeSoundEffectPlayer()
        val achievementsRepository = FakeAchievementsRepository()
        val viewModel = createViewModel(
            difficulty = GameDifficulty.HARD,
            sessionRepository = sessionRepository,
            achievementsRepository = achievementsRepository,
            soundPlayer = soundPlayer,
        )
        runCurrent()

        advanceTimeBy(60_000)
        runCurrent()

        val state = viewModel.uiState.value
        assertTrue(state.revealGuessingWord)
        assertEquals(0, state.attemptsLeftToGuess)
        assertFalse(state.showGameLostDialog)

        advanceTimeBy(3_000)
        runCurrent()
        assertTrue(viewModel.uiState.value.showGameLostDialog)

        assertEquals(1, sessionRepository.savedRequests.size)
        assertTrue(soundPlayer.playedEffects.contains(GameSoundEffect.GAME_LOST))
        assertTrue(
            achievementsRepository.currentProgress().any { progress ->
                progress.achievementId == AchievementId.FIRST_BLOOD && progress.isUnlocked
            }
        )
    }

    @Test
    fun `attempt loss reveals execution immediately and delays loss dialog`() = runTest(dispatcher) {
        val viewModel = createViewModel(difficulty = GameDifficulty.EASY)
        runCurrent()

        repeat(MAX_ATTEMPTS_PER_LEVEL) {
            val state = viewModel.uiState.value
            if (state.revealGuessingWord) return@repeat
            val wrongAlphabet = state.alphabetsList.first { alphabet ->
                !alphabet.isAlphabetGuessed &&
                    !state.wordToGuess.contains(alphabet.alphabet, ignoreCase = true)
            }
            viewModel.onEvent(GameEvent.AlphabetClicked(wrongAlphabet.alphabetId))
            runCurrent()
        }

        val stateAfterLoss = viewModel.uiState.value
        assertTrue(stateAfterLoss.revealGuessingWord)
        assertEquals(0, stateAfterLoss.attemptsLeftToGuess)
        assertFalse(stateAfterLoss.showGameLostDialog)
        assertTrue(stateAfterLoss.isInteractionLocked)

        viewModel.onEvent(GameEvent.BackPressed)
        runCurrent()
        assertFalse(viewModel.uiState.value.showExitDialog)

        advanceTimeBy(3_000)
        runCurrent()
        assertTrue(viewModel.uiState.value.showGameLostDialog)
        assertFalse(viewModel.uiState.value.isInteractionLocked)
    }

    @Test
    fun `non-final level completion transitions shimmer then return before next level`() = runTest(dispatcher) {
        val viewModel = createViewModel(difficulty = GameDifficulty.EASY)
        runCurrent()

        val initialLevel = viewModel.uiState.value.currentPlayerLevel
        val initialWord = viewModel.uiState.value.wordToGuess

        fun solveCurrentWord() {
            val state = viewModel.uiState.value
            val letters = state.wordToGuess
                .filter { it.isLetter() }
                .map { it.lowercaseChar().toString() }
                .distinct()
            letters.forEach { letter ->
                val latest = viewModel.uiState.value
                if (latest.gameOverByWinning || latest.revealGuessingWord) return@forEach
                val alphabet = latest.alphabetsList.firstOrNull { candidate ->
                    candidate.alphabet.equals(letter, ignoreCase = true) && !candidate.isAlphabetGuessed
                } ?: return@forEach
                viewModel.onEvent(GameEvent.AlphabetClicked(alphabet.alphabetId))
                runCurrent()
            }
        }

        solveCurrentWord()

        val holdingState = viewModel.uiState.value
        assertTrue(holdingState.isInteractionLocked)
        assertEquals(LevelTransitionPhase.SUCCESS_SHIMMER, holdingState.levelTransitionPhase)
        assertEquals(initialLevel, holdingState.currentPlayerLevel)
        assertEquals(initialWord, holdingState.wordToGuess)

        viewModel.onEvent(GameEvent.BackPressed)
        runCurrent()
        assertFalse(viewModel.uiState.value.showExitDialog)

        advanceTimeBy(999)
        runCurrent()
        assertEquals(LevelTransitionPhase.SUCCESS_SHIMMER, viewModel.uiState.value.levelTransitionPhase)
        assertEquals(initialLevel, viewModel.uiState.value.currentPlayerLevel)

        advanceTimeBy(1)
        runCurrent()
        assertEquals(LevelTransitionPhase.SUCCESS_RETURN, viewModel.uiState.value.levelTransitionPhase)
        assertTrue(viewModel.uiState.value.isInteractionLocked)
        assertEquals(initialLevel, viewModel.uiState.value.currentPlayerLevel)

        advanceTimeBy(999)
        runCurrent()
        assertTrue(viewModel.uiState.value.isInteractionLocked)
        assertEquals(LevelTransitionPhase.SUCCESS_RETURN, viewModel.uiState.value.levelTransitionPhase)

        advanceTimeBy(1)
        runCurrent()
        val nextLevelState = viewModel.uiState.value
        assertFalse(nextLevelState.isInteractionLocked)
        assertEquals(LevelTransitionPhase.NONE, nextLevelState.levelTransitionPhase)
        assertEquals(initialLevel + 1, nextLevelState.currentPlayerLevel)
        assertEquals(60_000L, nextLevelState.levelTimeRemainingMillis)
    }

    @Test
    fun `final win dialog is delayed for 3 seconds and interaction stays locked during hold`() = runTest(dispatcher) {
        val viewModel = createViewModel(difficulty = GameDifficulty.EASY)
        runCurrent()

        fun solveCurrentWord() {
            val state = viewModel.uiState.value
            val letters = state.wordToGuess
                .filter { it.isLetter() }
                .map { it.lowercaseChar().toString() }
                .distinct()
            letters.forEach { letter ->
                val latest = viewModel.uiState.value
                if (latest.gameOverByWinning || latest.revealGuessingWord) return@forEach
                val alphabet = latest.alphabetsList.firstOrNull { candidate ->
                    candidate.alphabet.equals(letter, ignoreCase = true) && !candidate.isAlphabetGuessed
                } ?: return@forEach
                viewModel.onEvent(GameEvent.AlphabetClicked(alphabet.alphabetId))
                runCurrent()
            }
        }

        while (!viewModel.uiState.value.gameOverByWinning) {
            solveCurrentWord()
            if (
                viewModel.uiState.value.levelTransitionPhase == LevelTransitionPhase.SUCCESS_SHIMMER ||
                viewModel.uiState.value.levelTransitionPhase == LevelTransitionPhase.SUCCESS_RETURN
            ) {
                advanceTimeBy(2_000)
                runCurrent()
            }
        }

        val winHoldState = viewModel.uiState.value
        assertTrue(winHoldState.gameOverByWinning)
        assertFalse(winHoldState.showGameWonDialog)
        assertTrue(winHoldState.isInteractionLocked)
        assertEquals(LevelTransitionPhase.FINAL_WIN_HOLD, winHoldState.levelTransitionPhase)

        advanceTimeBy(2_999)
        runCurrent()
        assertFalse(viewModel.uiState.value.showGameWonDialog)

        advanceTimeBy(1)
        runCurrent()
        val revealedWinDialogState = viewModel.uiState.value
        assertTrue(revealedWinDialogState.showGameWonDialog)
        assertFalse(revealedWinDialogState.isInteractionLocked)
        assertEquals(LevelTransitionPhase.NONE, revealedWinDialogState.levelTransitionPhase)
    }

    @Test
    fun `first blood unlock persists across relaunch and is not re-notified`() = runTest(dispatcher) {
        val sharedAchievementsRepository = FakeAchievementsRepository()

        val firstViewModel = createViewModel(
            difficulty = GameDifficulty.HARD,
            achievementsRepository = sharedAchievementsRepository,
        )
        advanceUntilIdle()

        advanceTimeBy(60_000)
        runCurrent()
        advanceUntilIdle()

        val firstBloodAfterFirstGame = sharedAchievementsRepository.currentProgress()
            .first { progress -> progress.achievementId == AchievementId.FIRST_BLOOD }
        assertTrue(firstBloodAfterFirstGame.isUnlocked)
        val firstUnlockAt = firstBloodAfterFirstGame.unlockedAtEpochMillis
        assertNotNull(firstUnlockAt)

        val secondViewModel = createViewModel(
            difficulty = GameDifficulty.HARD,
            achievementsRepository = sharedAchievementsRepository,
        )
        advanceUntilIdle()

        advanceTimeBy(60_000)
        runCurrent()
        advanceUntilIdle()

        val firstBloodAfterSecondGame = sharedAchievementsRepository.currentProgress()
            .first { progress -> progress.achievementId == AchievementId.FIRST_BLOOD }
        assertTrue(firstBloodAfterSecondGame.isUnlocked)
        assertEquals(firstUnlockAt, firstBloodAfterSecondGame.unlockedAtEpochMillis)
    }

    @Test
    fun `sound effects are not played when disabled in settings`() = runTest(dispatcher) {
        val soundPlayer = FakeSoundEffectPlayer()
        val viewModel = createViewModel(
            difficulty = GameDifficulty.EASY,
            soundEffectsEnabled = false,
            soundPlayer = soundPlayer,
        )
        advanceUntilIdle()

        val firstGuess = viewModel.uiState.value.alphabetsList.first().alphabetId
        viewModel.onEvent(GameEvent.AlphabetClicked(firstGuess))
        runCurrent()

        assertTrue(soundPlayer.playedEffects.isEmpty())
    }

    private fun createViewModel(
        difficulty: GameDifficulty = GameDifficulty.EASY,
        category: GameCategory = GameCategory.COUNTRIES,
        soundEffectsEnabled: Boolean = true,
        gameProgressVisualPreference: GameProgressVisualPreference =
            GameProgressVisualPreference.default,
        sessionRepository: FakeGameSessionRepository = FakeGameSessionRepository(),
        achievementsRepository: FakeAchievementsRepository = FakeAchievementsRepository(),
        soundPlayer: FakeSoundEffectPlayer = FakeSoundEffectPlayer(),
        notificationCoordinator: AchievementNotificationCoordinator = FakeAchievementNotificationCoordinator(),
    ): GameViewModel {
        return GameViewModel(
            settingsRepository = FakeGameSettingsRepository(
                difficulty = difficulty,
                category = category,
                soundEffectsEnabled = soundEffectsEnabled,
                gameProgressVisualPreference = gameProgressVisualPreference,
            ),
            sessionRepository = sessionRepository,
            achievementsRepository = achievementsRepository,
            soundEffectPlayer = soundPlayer,
            achievementNotificationCoordinator = notificationCoordinator,
        )
    }
}

private class FakeGameSettingsRepository(
    private var difficulty: GameDifficulty,
    private var category: GameCategory,
    private var soundEffectsEnabled: Boolean,
    private var gameProgressVisualPreference: GameProgressVisualPreference,
) : GameSettingsRepository {
    private val themePaletteState = MutableStateFlow(ThemePaletteId.INSANE_RED)
    private val languageState = MutableStateFlow(AppLanguage.default)
    private val cursorStyleState = MutableStateFlow(CursorStyle.default)

    override suspend fun getGameDifficulty(): GameDifficulty = difficulty

    override suspend fun getGameCategory(): GameCategory = category

    override suspend fun getThemePaletteId(): ThemePaletteId = ThemePaletteId.INSANE_RED

    override suspend fun getAppLanguage(): AppLanguage = AppLanguage.default

    override suspend fun isBackgroundMusicEnabled(): Boolean = true

    override suspend fun isSoundEffectsEnabled(): Boolean = soundEffectsEnabled

    override suspend fun getCursorStyle(): CursorStyle = cursorStyleState.value

    override suspend fun getGameProgressVisualPreference(): GameProgressVisualPreference {
        return gameProgressVisualPreference
    }

    override fun observeThemePaletteId(): StateFlow<ThemePaletteId> = themePaletteState

    override fun observeAppLanguage(): StateFlow<AppLanguage> = languageState

    override fun observeCursorStyle(): StateFlow<CursorStyle> = cursorStyleState

    override suspend fun setGameDifficulty(gameDifficulty: GameDifficulty) {
        difficulty = gameDifficulty
    }

    override suspend fun setGameCategory(gameCategory: GameCategory) {
        category = gameCategory
    }

    override suspend fun setThemePaletteId(themePaletteId: ThemePaletteId) {
        themePaletteState.value = themePaletteId
    }

    override suspend fun setAppLanguage(appLanguage: AppLanguage) {
        languageState.value = appLanguage
    }

    override suspend fun setBackgroundMusicEnabled(isEnabled: Boolean) = Unit

    override suspend fun setSoundEffectsEnabled(isEnabled: Boolean) {
        soundEffectsEnabled = isEnabled
    }

    override suspend fun setCursorStyle(cursorStyle: CursorStyle) {
        cursorStyleState.value = cursorStyle
    }

    override suspend fun setGameProgressVisualPreference(
        gameProgressVisualPreference: GameProgressVisualPreference,
    ) {
        this.gameProgressVisualPreference = gameProgressVisualPreference
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

private class FakeAchievementsRepository : AchievementsRepository {
    private val progress = MutableStateFlow<List<AchievementProgress>>(emptyList())
    private val counters = MutableStateFlow(AchievementStatCounters())

    override fun observeAchievementProgress() = progress

    override suspend fun replaceAchievementProgress(progress: List<AchievementProgress>) {
        this.progress.value = progress
    }

    override fun observeAchievementStatCounters() = counters

    override suspend fun saveAchievementStatCounters(counters: AchievementStatCounters) {
        this.counters.value = counters
    }

    fun currentProgress(): List<AchievementProgress> = progress.value
}

private class FakeAchievementNotificationCoordinator : AchievementNotificationCoordinator {
    override val bannerState = MutableStateFlow(AchievementBannerUiState())

    override fun enqueueUnlocked(unlockedIds: List<AchievementId>) = Unit
}
