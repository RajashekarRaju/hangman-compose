package com.developersbreach.hangman.ui.settings

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.AppLanguage
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
        )
        val viewModel = SettingsViewModel(settingsRepo)
        advanceUntilIdle()

        with(viewModel.uiState.value) {
            assertEquals(GameDifficulty.VERY_HARD, gameDifficulty)
            assertEquals(GameCategory.ANIMALS, gameCategory)
            assertEquals(ThemePaletteId.EMERALD, themePaletteId)
            assertEquals(AppLanguage.HINDI, selectedLanguage)
            assertEquals(false, isBackgroundMusicEnabled)
            assertEquals(false, isSoundEffectsEnabled)
            assertEquals(4f, pendingDifficultySliderPosition)
            assertEquals(SettingsSection.DIFFICULTY, selectedSettingsSection)
        }
    }

    @Test
    fun `settings updates persist as expected`() = runTest(dispatcher) {
        val settingsRepo = FakeSettingsRepository()
        val viewModel = SettingsViewModel(settingsRepo)
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.DifficultySliderPositionChanged(4f))
        viewModel.onEvent(SettingsEvent.DifficultyChanged(GameDifficulty.VERY_HARD))

        viewModel.onEvent(SettingsEvent.CategoryChanged(GameCategory.ANIMALS))

        viewModel.onEvent(SettingsEvent.LanguageChanged(AppLanguage.HINDI))

        viewModel.onEvent(SettingsEvent.ThemePaletteChanged(ThemePaletteId.EMERALD))
        viewModel.onEvent(SettingsEvent.BackgroundMusicToggled(false))
        viewModel.onEvent(SettingsEvent.SoundEffectsToggled(false))

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
    }

    @Test
    fun `navigate up emits effect`() = runTest(dispatcher) {
        val viewModel = SettingsViewModel(FakeSettingsRepository())
        advanceUntilIdle()

        val effect = async { viewModel.effects.first() }
        viewModel.onEvent(SettingsEvent.NavigateUpClicked)
        runCurrent()

        assertEquals(SettingsEffect.NavigateUp, effect.await())
    }

    @Test
    fun `section selected updates ui state`() = runTest(dispatcher) {
        val viewModel = SettingsViewModel(FakeSettingsRepository())
        advanceUntilIdle()

        viewModel.onEvent(SettingsEvent.SettingsSectionSelected(SettingsSection.LANGUAGE))

        assertEquals(SettingsSection.LANGUAGE, viewModel.uiState.value.selectedSettingsSection)
    }
}

private class FakeSettingsRepository(
    private var difficulty: GameDifficulty = GameDifficulty.EASY,
    private var category: GameCategory = GameCategory.COUNTRIES,
    private var themePaletteId: ThemePaletteId = ThemePaletteId.INSANE_RED,
    private var appLanguage: AppLanguage = AppLanguage.default,
    private var isBackgroundMusicEnabled: Boolean = true,
    private var isSoundEffectsEnabled: Boolean = true,
) : GameSettingsRepository {

    private val themeState = MutableStateFlow(themePaletteId)
    private val languageState = MutableStateFlow(appLanguage)

    var lastSetDifficulty: GameDifficulty? = null
    var lastSetCategory: GameCategory? = null
    var lastSetLanguage: AppLanguage? = null
    var lastSetPalette: ThemePaletteId? = null
    var lastSetBackgroundMusicEnabled: Boolean? = null
    var lastSetSoundEffectsEnabled: Boolean? = null

    override suspend fun getGameDifficulty(): GameDifficulty = difficulty

    override suspend fun getGameCategory(): GameCategory = category

    override suspend fun getThemePaletteId(): ThemePaletteId = themePaletteId

    override suspend fun getAppLanguage(): AppLanguage = appLanguage

    override suspend fun isBackgroundMusicEnabled(): Boolean = isBackgroundMusicEnabled

    override suspend fun isSoundEffectsEnabled(): Boolean = isSoundEffectsEnabled

    override fun observeThemePaletteId(): StateFlow<ThemePaletteId> = themeState

    override fun observeAppLanguage(): StateFlow<AppLanguage> = languageState

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
}
