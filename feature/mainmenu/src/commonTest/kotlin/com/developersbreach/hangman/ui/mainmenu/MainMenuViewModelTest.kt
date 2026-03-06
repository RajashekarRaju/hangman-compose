package com.developersbreach.hangman.ui.mainmenu

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.achievements.AchievementCatalog
import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.game.core.achievements.AchievementProgress
import com.developersbreach.game.core.achievements.AchievementStatCounters
import com.developersbreach.game.core.achievements.initialProgress
import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.repository.AchievementsRepository
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.repository.CursorStyle
import com.developersbreach.hangman.repository.GameProgressVisualPreference
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.ThemeMode
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
class MainMenuViewModelTest {

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
        val viewModel = MainMenuViewModel(historyRepo, achievementsRepo, settingsRepo, audio)

        historyRepo.emitHistory(
            historyRecord(score = 42),
            historyRecord(score = 155),
            historyRecord(score = 98),
        )
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(155, highScore)
        }
        assertTrue(audio.isPlaying())
    }

    @Test
    fun `navigate events emit effects and stop audio where needed`() = runTest(dispatcher) {
        val historyRepo = FakeHistoryRepository()
        val achievementsRepo = FakeAchievementsRepository()
        val settingsRepo = FakeSettingsRepository()
        val audio = FakeBackgroundAudioController()
        val viewModel = MainMenuViewModel(historyRepo, achievementsRepo, settingsRepo, audio)
        advanceUntilIdle()

        val gameEffect = async { viewModel.effects.first() }
        viewModel.onEvent(MainMenuEvent.NavigateToGameClicked)
        runCurrent()
        assertEquals(MainMenuEffect.NavigateToGame, gameEffect.await())
        assertFalse(audio.isPlaying())

        val historyEffect = async { viewModel.effects.first() }
        viewModel.onEvent(MainMenuEvent.NavigateToHistoryClicked)
        runCurrent()
        assertEquals(MainMenuEffect.NavigateToHistory, historyEffect.await())

        val settingsEffect = async { viewModel.effects.first() }
        viewModel.onEvent(MainMenuEvent.NavigateToSettingsClicked)
        runCurrent()
        assertEquals(MainMenuEffect.NavigateToSettings, settingsEffect.await())

        val achievementsEffect = async { viewModel.effects.first() }
        viewModel.onEvent(MainMenuEvent.NavigateToAchievementsClicked)
        runCurrent()
        assertEquals(MainMenuEffect.NavigateToAchievements, achievementsEffect.await())

        val guideEffect = async { viewModel.effects.first() }
        viewModel.onEvent(MainMenuEvent.NavigateToGameGuideClicked)
        runCurrent()
        assertEquals(MainMenuEffect.NavigateToGameGuide, guideEffect.await())

        val reportIssueEffect = async { viewModel.effects.first() }
        viewModel.onEvent(MainMenuEvent.ReportIssueClicked)
        runCurrent()
        assertEquals(
            MainMenuEffect.OpenIssueTracker(
                "https://github.com/RajashekarRaju/hangman-compose/issues"
            ),
            reportIssueEffect.await()
        )

        val finishEffect = async { viewModel.effects.first() }
        viewModel.onEvent(MainMenuEvent.ExitClicked)
        runCurrent()
        assertEquals(MainMenuEffect.FinishActivity, finishEffect.await())
        assertFalse(audio.isPlaying())
    }

    @Test
    fun `init keeps music off when disabled in settings`() = runTest(dispatcher) {
        val settingsRepository = FakeSettingsRepository(
            isBackgroundMusicEnabled = false,
        )
        val audioController = FakeBackgroundAudioController()
        val viewModel = MainMenuViewModel(
            historyRepository = FakeHistoryRepository(),
            achievementsRepository = FakeAchievementsRepository(),
            settingsRepository = settingsRepository,
            audioController = audioController,
        )
        advanceUntilIdle()

        assertFalse(audioController.isPlaying())
    }

    @Test
    fun `unread achievements count reflects unlocked unread progress`() = runTest(dispatcher) {
        val achievementsRepo = FakeAchievementsRepository()
        val viewModel = MainMenuViewModel(
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
    private var themeMode: ThemeMode = ThemeMode.default,
    private var appLanguage: AppLanguage = AppLanguage.default,
    private var isBackgroundMusicEnabled: Boolean = true,
    private var isSoundEffectsEnabled: Boolean = true,
    private var gameProgressVisualPreference: GameProgressVisualPreference =
        GameProgressVisualPreference.default,
) : GameSettingsRepository {
    private val themeState = MutableStateFlow(themePaletteId)
    private val themeModeState = MutableStateFlow(themeMode)
    private val languageState = MutableStateFlow(appLanguage)
    private val cursorStyleState = MutableStateFlow(CursorStyle.default)

    var lastSetDifficulty: GameDifficulty? = null
    var lastSetCategory: GameCategory? = null
    var lastSetLanguage: AppLanguage? = null

    override suspend fun getGameDifficulty(): GameDifficulty = difficulty

    override suspend fun getGameCategory(): GameCategory = category

    override suspend fun getThemePaletteId(): ThemePaletteId = themePaletteId

    override suspend fun getThemeMode(): ThemeMode = themeMode

    override suspend fun getAppLanguage(): AppLanguage = appLanguage

    override suspend fun isBackgroundMusicEnabled(): Boolean = isBackgroundMusicEnabled

    override suspend fun isSoundEffectsEnabled(): Boolean = isSoundEffectsEnabled

    override suspend fun getCursorStyle(): CursorStyle = cursorStyleState.value

    override suspend fun getGameProgressVisualPreference(): GameProgressVisualPreference {
        return gameProgressVisualPreference
    }

    override fun observeThemePaletteId(): StateFlow<ThemePaletteId> = themeState

    override fun observeThemeMode(): StateFlow<ThemeMode> = themeModeState

    override fun observeAppLanguage(): StateFlow<AppLanguage> = languageState

    override fun observeCursorStyle(): StateFlow<CursorStyle> = cursorStyleState

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

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        this.themeMode = themeMode
        themeModeState.value = themeMode
    }

    override suspend fun setAppLanguage(appLanguage: AppLanguage) {
        this.appLanguage = appLanguage
        languageState.value = appLanguage
        lastSetLanguage = appLanguage
    }

    override suspend fun setBackgroundMusicEnabled(isEnabled: Boolean) {
        isBackgroundMusicEnabled = isEnabled
    }

    override suspend fun setSoundEffectsEnabled(isEnabled: Boolean) {
        isSoundEffectsEnabled = isEnabled
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
