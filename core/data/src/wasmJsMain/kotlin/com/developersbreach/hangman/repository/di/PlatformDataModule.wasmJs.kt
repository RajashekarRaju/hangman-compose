package com.developersbreach.hangman.repository.di

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.achievements.AchievementStatCounters
import com.developersbreach.game.core.achievements.AchievementProgress
import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.audio.GameSoundEffect
import com.developersbreach.hangman.audio.GameSoundEffectPlayer
import com.developersbreach.hangman.repository.AchievementsRepository
import com.developersbreach.hangman.repository.GameSessionRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.HistoryRepository
import com.developersbreach.hangman.repository.metadata.generateHistoryMetadata
import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest
import com.developersbreach.hangman.repository.model.HistoryRecord
import com.developersbreach.hangman.repository.storage.StoredHistoryRecord
import com.developersbreach.hangman.repository.storage.StoredAchievementProgress
import com.developersbreach.hangman.repository.storage.StoredAchievementStatCounters
import com.developersbreach.hangman.repository.storage.StoredSettings
import com.developersbreach.hangman.repository.storage.toDomain
import com.developersbreach.hangman.repository.storage.toStored
import com.developersbreach.hangman.repository.storage.toGameCategory
import com.developersbreach.hangman.repository.storage.toGameDifficulty
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.toThemePaletteId
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.w3c.dom.HTMLAudioElement
import org.w3c.dom.events.Event
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDataModule(): Module = module {
    single { WasmLocalStorageGameRepository() }
    single<HistoryRepository> { get<WasmLocalStorageGameRepository>() }
    single<GameSessionRepository> { get<WasmLocalStorageGameRepository>() }
    single<AchievementsRepository> { WasmLocalStorageAchievementsRepository() }
    single<GameSettingsRepository> { WasmLocalStorageGameSettingsRepository() }
    single<BackgroundAudioController> { WasmBackgroundAudioController() }
    single<GameSoundEffectPlayer> { WasmGameSoundEffectPlayer() }
}

private const val HISTORY_KEY = "hangman.history.v1"
private const val SETTINGS_KEY = "hangman.settings.v1"
private const val ACHIEVEMENTS_KEY = "hangman.achievements.v1"
private const val ACHIEVEMENT_STATS_KEY = "hangman.achievement.stats.v1"

private val json = Json { ignoreUnknownKeys = true }

private class WasmLocalStorageGameRepository : HistoryRepository, GameSessionRepository {
    private val historyState = MutableStateFlow(loadHistory())

    private fun loadHistory(): List<HistoryRecord> {
        val raw = window.localStorage.getItem(HISTORY_KEY) ?: return emptyList()
        val stored = runCatching {
            json.decodeFromString<List<StoredHistoryRecord>>(raw)
        }.getOrDefault(emptyList())
        return stored.map { it.toDomain() }
    }

    private fun persist(history: List<HistoryRecord>) {
        val payload = json.encodeToString(history.map { it.toStored() })
        window.localStorage.setItem(HISTORY_KEY, payload)
    }

    override fun observeHistory(): Flow<List<HistoryRecord>> = historyState

    override suspend fun deleteHistoryItem(history: HistoryRecord) {
        val updated = historyState.value.filterNot { it.gameId == history.gameId }
        historyState.value = updated
        persist(updated)
    }

    override suspend fun deleteAllHistory() {
        historyState.value = emptyList()
        window.localStorage.removeItem(HISTORY_KEY)
    }

    override suspend fun saveCompletedGame(request: GameHistoryWriteRequest) {
        val metadata = generateHistoryMetadata(historyState.value.size)
        val record = HistoryRecord(
            gameId = metadata.gameId,
            gameScore = request.gameScore,
            gameLevel = request.gameLevel,
            gameDifficulty = request.gameDifficulty,
            gameCategory = request.gameCategory,
            gameSummary = request.gameSummary,
            gamePlayedTime = metadata.gamePlayedTime,
            gamePlayedDate = metadata.gamePlayedDate,
            hintsUsed = request.hintsUsed,
            hintTypesUsed = request.hintTypesUsed,
        )
        val updated = listOf(record) + historyState.value
        historyState.value = updated
        persist(updated)
    }
}

private class WasmLocalStorageGameSettingsRepository : GameSettingsRepository {
    private var settings: StoredSettings = loadSettings()
    private val themePaletteIdState = MutableStateFlow(settings.themePaletteId.toThemePaletteId())

    private fun loadSettings(): StoredSettings {
        val raw = window.localStorage.getItem(SETTINGS_KEY) ?: return StoredSettings()
        return runCatching { json.decodeFromString<StoredSettings>(raw) }.getOrDefault(StoredSettings())
    }

    private fun persist() {
        window.localStorage.setItem(SETTINGS_KEY, json.encodeToString(settings))
    }

    override suspend fun getGameDifficulty(): GameDifficulty = settings.gameDifficulty.toGameDifficulty()

    override suspend fun getGameCategory(): GameCategory = settings.gameCategory.toGameCategory()

    override suspend fun getThemePaletteId(): ThemePaletteId {
        return settings.themePaletteId.toThemePaletteId().also { themePaletteIdState.value = it }
    }

    override fun observeThemePaletteId(): StateFlow<ThemePaletteId> = themePaletteIdState.asStateFlow()

    override suspend fun setGameDifficulty(gameDifficulty: GameDifficulty) {
        settings = settings.copy(gameDifficulty = gameDifficulty.name)
        persist()
    }

    override suspend fun setGameCategory(gameCategory: GameCategory) {
        settings = settings.copy(gameCategory = gameCategory.name)
        persist()
    }

    override suspend fun setThemePaletteId(themePaletteId: ThemePaletteId) {
        settings = settings.copy(themePaletteId = themePaletteId.name)
        persist()
        themePaletteIdState.value = themePaletteId
    }
}

private class WasmLocalStorageAchievementsRepository : AchievementsRepository {

    private val achievementProgressState = MutableStateFlow(loadAchievementProgress())
    private val achievementStatsState = MutableStateFlow(loadAchievementStats())

    private fun loadAchievementProgress(): List<AchievementProgress> {
        val raw = window.localStorage.getItem(ACHIEVEMENTS_KEY) ?: return emptyList()
        val stored = runCatching {
            json.decodeFromString<List<StoredAchievementProgress>>(raw)
        }.getOrDefault(emptyList())
        return stored.mapNotNull { value -> value.toDomain() }
    }

    private fun loadAchievementStats(): AchievementStatCounters {
        val raw = window.localStorage.getItem(ACHIEVEMENT_STATS_KEY) ?: return AchievementStatCounters()
        val stored = runCatching {
            json.decodeFromString<StoredAchievementStatCounters>(raw)
        }.getOrDefault(StoredAchievementStatCounters())
        return stored.toDomain()
    }

    private fun persistProgress(progress: List<AchievementProgress>) {
        window.localStorage.setItem(
            ACHIEVEMENTS_KEY,
            json.encodeToString(progress.map { value -> value.toStored() }),
        )
    }

    private fun persistStats(counters: AchievementStatCounters) {
        window.localStorage.setItem(
            ACHIEVEMENT_STATS_KEY,
            json.encodeToString(counters.toStored()),
        )
    }

    override fun observeAchievementProgress(): Flow<List<AchievementProgress>> = achievementProgressState

    override suspend fun replaceAchievementProgress(progress: List<AchievementProgress>) {
        achievementProgressState.value = progress
        persistProgress(progress)
    }

    override fun observeAchievementStatCounters(): Flow<AchievementStatCounters> = achievementStatsState

    override suspend fun saveAchievementStatCounters(counters: AchievementStatCounters) {
        achievementStatsState.value = counters
        persistStats(counters)
    }
}

private class WasmBackgroundAudioController : BackgroundAudioController {
    private val player = createAudioPlayer("game_background_music.mp3", loop = true).apply {
        volume = 0.35
    }
    private var shouldBePlaying = false
    private var userInteracted = false
    private var gestureListenerRegistered = false
    private val gestureListener: (Event) -> Unit = {
        userInteracted = true
        unregisterGestureListeners()
        if (shouldBePlaying) {
            player.play()
        }
    }

    override fun playLoop() {
        shouldBePlaying = true
        when {
            userInteracted -> player.play()
            else -> registerGestureListeners()
        }
    }

    override fun stop() {
        shouldBePlaying = false
        player.pause()
        player.currentTime = 0.0
    }

    override fun isPlaying(): Boolean = !player.paused

    private fun registerGestureListeners() {
        if (gestureListenerRegistered) return
        gestureListenerRegistered = true
        document.addEventListener("pointerdown", gestureListener)
        document.addEventListener("mousedown", gestureListener)
        document.addEventListener("click", gestureListener)
        document.addEventListener("touchstart", gestureListener)
        document.addEventListener("keydown", gestureListener)
    }

    private fun unregisterGestureListeners() {
        if (!gestureListenerRegistered) return
        gestureListenerRegistered = false
        document.removeEventListener("pointerdown", gestureListener)
        document.removeEventListener("mousedown", gestureListener)
        document.removeEventListener("click", gestureListener)
        document.removeEventListener("touchstart", gestureListener)
        document.removeEventListener("keydown", gestureListener)
    }
}

private class WasmGameSoundEffectPlayer : GameSoundEffectPlayer {
    private val players: Map<GameSoundEffect, HTMLAudioElement> =
        GameSoundEffect.entries.associateWith { soundEffect ->
            createAudioPlayer("${soundEffect.resourceKey}.mp3")
        }

    override fun play(soundEffect: GameSoundEffect) {
        val player = players[soundEffect] ?: return
        player.pause()
        player.currentTime = 0.0
        player.play()
    }
}

private fun createAudioPlayer(src: String, loop: Boolean = false): HTMLAudioElement {
    return (document.createElement("audio") as HTMLAudioElement).apply {
        this.src = src
        this.loop = loop
        this.preload = "auto"
    }
}
