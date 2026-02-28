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
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSBundle
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSURL

actual fun platformDataModule(): Module = module {
    single { IosUserDefaultsGameRepository() }
    single<HistoryRepository> { get<IosUserDefaultsGameRepository>() }
    single<GameSessionRepository> { get<IosUserDefaultsGameRepository>() }
    single<AchievementsRepository> { IosUserDefaultsAchievementsRepository() }
    single<GameSettingsRepository> { IosUserDefaultsGameSettingsRepository() }
    single<BackgroundAudioController> { IosBackgroundAudioController() }
    single<GameSoundEffectPlayer> { IosGameSoundEffectPlayer() }
}

private const val HISTORY_KEY = "hangman.history.v1"
private const val SETTINGS_KEY = "hangman.settings.v1"
private const val ACHIEVEMENTS_KEY = "hangman.achievements.v1"
private const val ACHIEVEMENT_STATS_KEY = "hangman.achievement.stats.v1"

private val json = Json { ignoreUnknownKeys = true }
private val defaults = NSUserDefaults.standardUserDefaults

private class IosUserDefaultsGameRepository : HistoryRepository, GameSessionRepository {
    private val historyState = MutableStateFlow(loadHistory())

    private fun loadHistory(): List<HistoryRecord> {
        val raw = defaults.stringForKey(HISTORY_KEY) ?: return emptyList()
        val stored = runCatching {
            json.decodeFromString<List<StoredHistoryRecord>>(raw)
        }.getOrDefault(emptyList())
        return stored.map { it.toDomain() }
    }

    private fun persist(history: List<HistoryRecord>) {
        val payload = json.encodeToString(history.map { it.toStored() })
        defaults.setObject(payload, forKey = HISTORY_KEY)
    }

    override fun observeHistory(): Flow<List<HistoryRecord>> = historyState

    override suspend fun deleteHistoryItem(history: HistoryRecord) {
        val updated = historyState.value.filterNot { it.gameId == history.gameId }
        historyState.value = updated
        persist(updated)
    }

    override suspend fun deleteAllHistory() {
        historyState.value = emptyList()
        defaults.removeObjectForKey(HISTORY_KEY)
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

private class IosUserDefaultsGameSettingsRepository : GameSettingsRepository {
    private var settings: StoredSettings = loadSettings()
    private val themePaletteIdState = MutableStateFlow(settings.themePaletteId.toThemePaletteId())

    private fun loadSettings(): StoredSettings {
        val raw = defaults.stringForKey(SETTINGS_KEY) ?: return StoredSettings()
        return runCatching { json.decodeFromString<StoredSettings>(raw) }.getOrDefault(StoredSettings())
    }

    private fun persist() {
        defaults.setObject(json.encodeToString(settings), forKey = SETTINGS_KEY)
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

private class IosUserDefaultsAchievementsRepository : AchievementsRepository {

    private val achievementProgressState = MutableStateFlow(loadAchievementProgress())
    private val achievementStatsState = MutableStateFlow(loadAchievementStats())

    private fun loadAchievementProgress(): List<AchievementProgress> {
        val raw = defaults.stringForKey(ACHIEVEMENTS_KEY) ?: return emptyList()
        val stored = runCatching {
            json.decodeFromString<List<StoredAchievementProgress>>(raw)
        }.getOrDefault(emptyList())
        return stored.mapNotNull { value -> value.toDomain() }
    }

    private fun loadAchievementStats(): AchievementStatCounters {
        val raw = defaults.stringForKey(ACHIEVEMENT_STATS_KEY) ?: return AchievementStatCounters()
        val stored = runCatching {
            json.decodeFromString<StoredAchievementStatCounters>(raw)
        }.getOrDefault(StoredAchievementStatCounters())
        return stored.toDomain()
    }

    private fun persistProgress(progress: List<AchievementProgress>) {
        defaults.setObject(
            json.encodeToString(progress.map { value -> value.toStored() }),
            forKey = ACHIEVEMENTS_KEY,
        )
    }

    private fun persistStats(counters: AchievementStatCounters) {
        defaults.setObject(
            json.encodeToString(counters.toStored()),
            forKey = ACHIEVEMENT_STATS_KEY,
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

private class IosBackgroundAudioController : BackgroundAudioController {

    private val player = createAudioPlayer("game_background_music.mp3")?.apply {
        numberOfLoops = -1
        volume = 0.35f
    }

    override fun playLoop() {
        player?.play()
    }

    override fun stop() {
        player?.stop()
        player?.currentTime = 0.0
    }

    override fun isPlaying(): Boolean = player?.playing == true
}

private class IosGameSoundEffectPlayer : GameSoundEffectPlayer {
    private val players: Map<GameSoundEffect, AVAudioPlayer?> =
        GameSoundEffect.entries.associateWith { sound ->
            createAudioPlayer("${sound.resourceKey}.mp3")
        }

    override fun play(soundEffect: GameSoundEffect) {
        val player = players[soundEffect] ?: return
        player.stop()
        player.currentTime = 0.0
        player.play()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun createAudioPlayer(fileName: String): AVAudioPlayer? {
    val baseName = fileName.substringBeforeLast('.')
    val extension = fileName.substringAfterLast('.', missingDelimiterValue = "")
    val path = NSBundle.mainBundle.pathForResource(baseName, extension)
    if (path == null) {
        println("iOS audio resource not found in bundle: $fileName")
        return null
    }
    return runCatching {
        AVAudioPlayer(contentsOfURL = NSURL.fileURLWithPath(path), error = null).apply {
            prepareToPlay()
        }
    }.onFailure { error ->
        println("Failed to load iOS audio resource '$fileName': ${error.message}")
    }.getOrNull()
}