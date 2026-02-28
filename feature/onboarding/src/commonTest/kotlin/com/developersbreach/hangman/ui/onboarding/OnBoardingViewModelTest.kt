package com.developersbreach.hangman.ui.onboarding

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.achievements.AchievementCatalog
import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.game.core.achievements.AchievementProgress
import com.developersbreach.game.core.achievements.AchievementStatCounters
import com.developersbreach.game.core.achievements.initialProgress
import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.repository.AchievementsRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.HistoryRepository
import com.developersbreach.hangman.repository.model.HistoryRecord
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class OnBoardingViewModelTest {

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
    fun `init hydrates preferences and observes highest score`() = runTest(dispatcher) {
        val historyRepo = FakeHistoryRepository()
        val settingsRepo = FakeSettingsRepository(
            difficulty = GameDifficulty.VERY_HARD,
            category = GameCategory.ANIMALS,
        )
        val achievementsRepo = FakeAchievementsRepository()
        val audio = FakeBackgroundAudioController()
        val viewModel = OnBoardingViewModel(historyRepo, achievementsRepo, settingsRepo, audio)

        historyRepo.emitHistory(
            historyRecord(score = 42),
            historyRecord(score = 155),
            historyRecord(score = 98),
        )
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(GameDifficulty.VERY_HARD, gameDifficulty)
            assertEquals(GameCategory.ANIMALS, gameCategory)
            assertEquals(155, highScore)
            assertTrue(isBackgroundMusicPlaying)
            assertEquals(GameDifficulty.VERY_HARD, pendingDifficulty)
            assertEquals(4f, pendingDifficultySliderPosition)
        }
    }

    @Test
    fun `navigate events emit effects and stop audio where needed`() = runTest(dispatcher) {
        val historyRepo = FakeHistoryRepository()
        val achievementsRepo = FakeAchievementsRepository()
        val settingsRepo = FakeSettingsRepository()
        val audio = FakeBackgroundAudioController()
        val viewModel = OnBoardingViewModel(historyRepo, achievementsRepo, settingsRepo, audio)
        advanceUntilIdle()

        val gameEffect = async { viewModel.effects.first() }
        viewModel.onEvent(OnBoardingEvent.NavigateToGameClicked)
        runCurrent()
        assertEquals(OnBoardingEffect.NavigateToGame, gameEffect.await())
        assertFalse(audio.isPlaying())

        val historyEffect = async { viewModel.effects.first() }
        viewModel.onEvent(OnBoardingEvent.NavigateToHistoryClicked)
        runCurrent()
        assertEquals(OnBoardingEffect.NavigateToHistory, historyEffect.await())

        val achievementsEffect = async { viewModel.effects.first() }
        viewModel.onEvent(OnBoardingEvent.NavigateToAchievementsClicked)
        runCurrent()
        assertEquals(OnBoardingEffect.NavigateToAchievements, achievementsEffect.await())

        val reportIssueEffect = async { viewModel.effects.first() }
        viewModel.onEvent(OnBoardingEvent.ReportIssueClicked)
        runCurrent()
        assertEquals(
            OnBoardingEffect.OpenIssueTracker(
                "https://github.com/RajashekarRaju/hangman-compose/issues"
            ),
            reportIssueEffect.await()
        )

        val finishEffect = async { viewModel.effects.first() }
        viewModel.onEvent(OnBoardingEvent.ExitClicked)
        runCurrent()
        assertEquals(OnBoardingEffect.FinishActivity, finishEffect.await())
        assertFalse(audio.isPlaying())
    }

    @Test
    fun `difficulty and category changes update state and persist`() = runTest(dispatcher) {
        val historyRepo = FakeHistoryRepository()
        val achievementsRepo = FakeAchievementsRepository()
        val settingsRepo = FakeSettingsRepository()
        val audio = FakeBackgroundAudioController()
        val viewModel = OnBoardingViewModel(historyRepo, achievementsRepo, settingsRepo, audio)
        advanceUntilIdle()

        viewModel.onEvent(OnBoardingEvent.OpenDifficultyDialog)
        viewModel.onEvent(OnBoardingEvent.DifficultySliderPositionChanged(4f))
        viewModel.onEvent(OnBoardingEvent.DifficultyChanged(GameDifficulty.VERY_HARD))
        viewModel.onEvent(OnBoardingEvent.CategoryChanged(GameCategory.ANIMALS))
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertTrue(isDifficultyDialogOpen)
            assertEquals(GameDifficulty.VERY_HARD, gameDifficulty)
            assertEquals(4f, pendingDifficultySliderPosition)
            assertEquals(GameCategory.ANIMALS, gameCategory)
        }
        assertEquals(GameDifficulty.VERY_HARD, settingsRepo.lastSetDifficulty)
        assertEquals(GameCategory.ANIMALS, settingsRepo.lastSetCategory)
    }

    @Test
    fun `toggle background music updates playback state`() = runTest(dispatcher) {
        val viewModel = OnBoardingViewModel(
            historyRepository = FakeHistoryRepository(),
            achievementsRepository = FakeAchievementsRepository(),
            settingsRepository = FakeSettingsRepository(),
            audioController = FakeBackgroundAudioController(),
        )
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.isBackgroundMusicPlaying)

        viewModel.onEvent(OnBoardingEvent.ToggleBackgroundMusic)
        assertFalse(viewModel.uiState.value.isBackgroundMusicPlaying)

        viewModel.onEvent(OnBoardingEvent.ToggleBackgroundMusic)
        assertTrue(viewModel.uiState.value.isBackgroundMusicPlaying)
    }

    @Test
    fun `unread achievements count reflects unlocked unread progress`() = runTest(dispatcher) {
        val achievementsRepo = FakeAchievementsRepository()
        val viewModel = OnBoardingViewModel(
            historyRepository = FakeHistoryRepository(),
            achievementsRepository = achievementsRepo,
            settingsRepository = FakeSettingsRepository(),
            audioController = FakeBackgroundAudioController(),
        )
        advanceUntilIdle()

        achievementsRepo.emitProgress(
            progressFor(AchievementId.FIRST_BLOOD, isUnlocked = true, isUnread = true),
            progressFor(AchievementId.GRAVE_WALKER, isUnlocked = true, isUnread = false),
            progressFor(AchievementId.NIGHT_SHIFT, isUnlocked = false, isUnread = true),
        )
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.hasUnreadAchievements)
    }
}

private class FakeSettingsRepository(
    private var difficulty: GameDifficulty = GameDifficulty.EASY,
    private var category: GameCategory = GameCategory.COUNTRIES,
    private var themePaletteId: ThemePaletteId = ThemePaletteId.EMERALD,
) : GameSettingsRepository {
    private val themeState = MutableStateFlow(themePaletteId)

    var lastSetDifficulty: GameDifficulty? = null
    var lastSetCategory: GameCategory? = null

    override suspend fun getGameDifficulty(): GameDifficulty = difficulty

    override suspend fun getGameCategory(): GameCategory = category

    override suspend fun getThemePaletteId(): ThemePaletteId = themePaletteId

    override fun observeThemePaletteId(): StateFlow<ThemePaletteId> = themeState

    override suspend fun setGameDifficulty(gameDifficulty: GameDifficulty) {
        difficulty = gameDifficulty
        lastSetDifficulty = gameDifficulty
    }

    override suspend fun setGameCategory(gameCategory: GameCategory) {
        category = gameCategory
        lastSetCategory = gameCategory
    }

    override suspend fun setThemePaletteId(themePaletteId: ThemePaletteId) {
        this.themePaletteId = themePaletteId
        themeState.value = themePaletteId
    }
}

private class FakeHistoryRepository : HistoryRepository {

    private val state = MutableStateFlow<List<HistoryRecord>>(emptyList())

    override fun observeHistory() = state

    override suspend fun deleteHistoryItem(history: HistoryRecord) = Unit

    override suspend fun deleteAllHistory() = Unit

    fun emitHistory(vararg records: HistoryRecord) {
        state.update { records.toList() }
    }
}

private class FakeAchievementsRepository : AchievementsRepository {
    private val progressState = MutableStateFlow<List<AchievementProgress>>(emptyList())
    private val countersState = MutableStateFlow(AchievementStatCounters())

    override fun observeAchievementProgress() = progressState

    override suspend fun replaceAchievementProgress(progress: List<AchievementProgress>) {
        progressState.value = progress
    }

    override fun observeAchievementStatCounters() = countersState

    override suspend fun saveAchievementStatCounters(counters: AchievementStatCounters) {
        countersState.value = counters
    }

    fun emitProgress(vararg progress: AchievementProgress) {
        progressState.value = progress.toList()
    }
}

private class FakeBackgroundAudioController : BackgroundAudioController {
    private var playing = false

    override fun playLoop() {
        playing = true
    }

    override fun stop() {
        playing = false
    }

    override fun isPlaying(): Boolean = playing
}

private fun historyRecord(score: Int): HistoryRecord {
    return HistoryRecord(
        gameId = "g-$score",
        gameScore = score,
        gameLevel = 1,
        gameDifficulty = GameDifficulty.EASY,
        gameCategory = GameCategory.COUNTRIES,
        gameSummary = true,
        gamePlayedTime = "10:00 AM",
        gamePlayedDate = "2026-02-24",
    )
}

private fun progressFor(
    id: AchievementId,
    isUnlocked: Boolean,
    isUnread: Boolean,
): AchievementProgress {
    val definition = AchievementCatalog.definitionFor(id)
    return definition.initialProgress().copy(
        isUnlocked = isUnlocked,
        isUnread = isUnread,
        progressCurrent = if (isUnlocked) definition.target else 0,
    )
}
