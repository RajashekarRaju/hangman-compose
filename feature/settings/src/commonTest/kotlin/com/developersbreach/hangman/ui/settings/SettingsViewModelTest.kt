package com.developersbreach.hangman.ui.settings

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.repository.AppLanguage
import com.developersbreach.hangman.repository.CursorStyle
import com.developersbreach.hangman.repository.GameProgressVisualPreference
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
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

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private val backgroundAudioController = FakeBackgroundAudioController()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init hydrates settings from repository`() = runTest(dispatcher) {
        val settingsRepo = FakeSettingsRepository(
            difficulty = GameDifficulty.VERY_HARD,
            category = GameCategory.ANIMALS,
            themePaletteId = ThemePaletteId.EMERALD,
            appLanguage = AppLanguage.HINDI,
            isBackgroundMusicEnabled = false,
            isSoundEffectsEnabled = false,
            cursorStyle = CursorStyle.DEMON,
            gameProgressVisualPreference = GameProgressVisualPreference.LEVEL_POINTS_ATTEMPTS,
        )
        val viewModel = SettingsViewModel(settingsRepo, backgroundAudioController)
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(GameDifficulty.VERY_HARD, gameDifficulty)
            assertEquals(GameCategory.ANIMALS, gameCategory)
            assertEquals(ThemePaletteId.EMERALD, themePaletteId)
            assertEquals(AppLanguage.HINDI, selectedLanguage)
            assertEquals(false, isBackgroundMusicEnabled)
            assertEquals(false, isSoundEffectsEnabled)
            assertEquals(CursorStyle.DEMON, selectedCursorStyle)
            assertEquals(
                GameProgressVisualPreference.LEVEL_POINTS_ATTEMPTS,
                selectedGameProgressVisualPreference,
            )
            assertEquals(4f, pendingDifficultySliderPosition)
            assertEquals(SettingsSection.Difficulty, selectedSettingsSection)
        }
    }

    @Test
    fun `settings updates persist as expected`() = runTest(dispatcher) {
        val settingsRepo = FakeSettingsRepository()
        val viewModel = SettingsViewModel(settingsRepo, backgroundAudioController)
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.DifficultySliderPositionChanged(4f))
        viewModel.onEvent(SettingsEvent.DifficultyChanged(GameDifficulty.VERY_HARD))

        viewModel.onEvent(SettingsEvent.CategoryChanged(GameCategory.ANIMALS))

        viewModel.onEvent(SettingsEvent.LanguageChanged(AppLanguage.HINDI))

        viewModel.onEvent(SettingsEvent.ThemePaletteChanged(ThemePaletteId.EMERALD))
        viewModel.onEvent(SettingsEvent.BackgroundMusicToggled(false))
        viewModel.onEvent(SettingsEvent.SoundEffectsToggled(false))
        viewModel.onEvent(SettingsEvent.CursorStyleChanged(CursorStyle.SKULL))
        viewModel.onEvent(
            SettingsEvent.GameProgressVisualPreferenceChanged(
                GameProgressVisualPreference.LEVEL_POINTS_ATTEMPTS,
            ),
        )

        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(GameDifficulty.VERY_HARD, gameDifficulty)
        }

        assertEquals(GameDifficulty.VERY_HARD, settingsRepo.lastSetDifficulty)
        assertEquals(GameCategory.ANIMALS, settingsRepo.lastSetCategory)
        assertEquals(AppLanguage.HINDI, settingsRepo.lastSetLanguage)
        assertEquals(ThemePaletteId.EMERALD, settingsRepo.lastSetPalette)
        assertEquals(false, settingsRepo.lastSetBackgroundMusicEnabled)
        assertEquals(false, settingsRepo.lastSetSoundEffectsEnabled)
        assertEquals(CursorStyle.SKULL, settingsRepo.lastSetCursorStyle)
        assertEquals(
            GameProgressVisualPreference.LEVEL_POINTS_ATTEMPTS,
            settingsRepo.lastSetGameProgressVisualPreference,
        )
    }

    @Test
    fun `navigate up emits effect`() = runTest(dispatcher) {
        val viewModel = SettingsViewModel(FakeSettingsRepository(), backgroundAudioController)
        advanceUntilIdle()

        val effect = async { viewModel.effects.first() }
        viewModel.onEvent(SettingsEvent.NavigateUpClicked)
        runCurrent()

        assertEquals(SettingsEffect.NavigateUp, effect.await())
    }

    @Test
    fun `section selected updates ui state`() = runTest(dispatcher) {
        val viewModel = SettingsViewModel(FakeSettingsRepository(), backgroundAudioController)
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.SettingsSectionSelected(SettingsSection.Language))

        assertEquals(SettingsSection.Language, viewModel.uiState.value.selectedSettingsSection)
    }
}

private class FakeSettingsRepository(
    private var difficulty: GameDifficulty = GameDifficulty.EASY,
    private var category: GameCategory = GameCategory.COUNTRIES,
    private var themePaletteId: ThemePaletteId = ThemePaletteId.INSANE_RED,
    private var appLanguage: AppLanguage = AppLanguage.default,
    private var isBackgroundMusicEnabled: Boolean = true,
    private var isSoundEffectsEnabled: Boolean = true,
    private var cursorStyle: CursorStyle = CursorStyle.default,
    private var gameProgressVisualPreference: GameProgressVisualPreference =
        GameProgressVisualPreference.default,
) : GameSettingsRepository {

    private val themeState = MutableStateFlow(themePaletteId)
    private val languageState = MutableStateFlow(appLanguage)
    private val cursorStyleState = MutableStateFlow(cursorStyle)

    var lastSetDifficulty: GameDifficulty? = null
    var lastSetCategory: GameCategory? = null
    var lastSetLanguage: AppLanguage? = null
    var lastSetPalette: ThemePaletteId? = null
    var lastSetBackgroundMusicEnabled: Boolean? = null
    var lastSetSoundEffectsEnabled: Boolean? = null
    var lastSetCursorStyle: CursorStyle? = null
    var lastSetGameProgressVisualPreference: GameProgressVisualPreference? = null

    override suspend fun getGameDifficulty(): GameDifficulty = difficulty

    override suspend fun getGameCategory(): GameCategory = category

    override suspend fun getThemePaletteId(): ThemePaletteId = themePaletteId

    override suspend fun getAppLanguage(): AppLanguage = appLanguage

    override suspend fun isBackgroundMusicEnabled(): Boolean = isBackgroundMusicEnabled

    override suspend fun isSoundEffectsEnabled(): Boolean = isSoundEffectsEnabled

    override suspend fun getCursorStyle(): CursorStyle = cursorStyle

    override suspend fun getGameProgressVisualPreference(): GameProgressVisualPreference {
        return gameProgressVisualPreference
    }

    override fun observeThemePaletteId(): StateFlow<ThemePaletteId> = themeState

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
        lastSetPalette = themePaletteId
    }

    override suspend fun setAppLanguage(appLanguage: AppLanguage) {
        this.appLanguage = appLanguage
        languageState.value = appLanguage
        lastSetLanguage = appLanguage
    }

    override suspend fun setBackgroundMusicEnabled(isEnabled: Boolean) {
        isBackgroundMusicEnabled = isEnabled
        lastSetBackgroundMusicEnabled = isEnabled
    }

    override suspend fun setSoundEffectsEnabled(isEnabled: Boolean) {
        isSoundEffectsEnabled = isEnabled
        lastSetSoundEffectsEnabled = isEnabled
    }

    override suspend fun setCursorStyle(cursorStyle: CursorStyle) {
        this.cursorStyle = cursorStyle
        cursorStyleState.value = cursorStyle
        lastSetCursorStyle = cursorStyle
    }

    override suspend fun setGameProgressVisualPreference(
        gameProgressVisualPreference: GameProgressVisualPreference,
    ) {
        this.gameProgressVisualPreference = gameProgressVisualPreference
        lastSetGameProgressVisualPreference = gameProgressVisualPreference
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
