package com.developersbreach.hangman.composeapp

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.audio.GameSoundEffect
import com.developersbreach.hangman.audio.GameSoundEffectPlayer
import com.developersbreach.hangman.repository.GameSessionRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.HistoryRepository
import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest
import com.developersbreach.hangman.repository.model.HistoryRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSUUID
import platform.Foundation.NSUserDefaults

actual fun platformRepositoryModule(): Module = module {
    single { IosUserDefaultsGameRepository() }
    single<HistoryRepository> { get<IosUserDefaultsGameRepository>() }
    single<GameSessionRepository> { get<IosUserDefaultsGameRepository>() }
    single<GameSettingsRepository> { IosUserDefaultsGameSettingsRepository() }
    single<BackgroundAudioController> { IosNoOpBackgroundAudioController() }
    single<GameSoundEffectPlayer> { IosNoOpGameSoundEffectPlayer() }
}

private const val HISTORY_KEY = "hangman.history.v1"
private const val SETTINGS_KEY = "hangman.settings.v1"

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
        val payload = json.encodeToString(history.map { StoredHistoryRecord.fromDomain(it) })
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
        val record = HistoryRecord(
            gameId = "ios-${NSUUID().UUIDString}",
            gameScore = request.gameScore,
            gameLevel = request.gameLevel,
            gameDifficulty = request.gameDifficulty,
            gameCategory = request.gameCategory,
            gameSummary = request.gameSummary,
            gamePlayedTime = timeNow(),
            gamePlayedDate = dateNow(),
        )
        val updated = listOf(record) + historyState.value
        historyState.value = updated
        persist(updated)
    }
}

private class IosUserDefaultsGameSettingsRepository : GameSettingsRepository {
    private var settings: StoredSettings = loadSettings()

    private fun loadSettings(): StoredSettings {
        val raw = defaults.stringForKey(SETTINGS_KEY) ?: return StoredSettings()
        return runCatching { json.decodeFromString<StoredSettings>(raw) }.getOrDefault(StoredSettings())
    }

    private fun persist() {
        defaults.setObject(json.encodeToString(settings), forKey = SETTINGS_KEY)
    }

    private fun toDifficulty(value: String): GameDifficulty {
        return runCatching { GameDifficulty.valueOf(value) }.getOrDefault(GameDifficulty.EASY)
    }

    private fun toCategory(value: String): GameCategory {
        return runCatching { GameCategory.valueOf(value) }.getOrDefault(GameCategory.COUNTRIES)
    }

    override suspend fun getGameDifficulty(): GameDifficulty = toDifficulty(settings.gameDifficulty)

    override suspend fun getGameCategory(): GameCategory = toCategory(settings.gameCategory)

    override suspend fun setGameDifficulty(gameDifficulty: GameDifficulty) {
        settings = settings.copy(gameDifficulty = gameDifficulty.name)
        persist()
    }

    override suspend fun setGameCategory(gameCategory: GameCategory) {
        settings = settings.copy(gameCategory = gameCategory.name)
        persist()
    }
}

private class IosNoOpBackgroundAudioController : BackgroundAudioController {
    private var playing = false

    override fun playLoop() {
        playing = true
    }

    override fun stop() {
        playing = false
    }

    override fun isPlaying(): Boolean = playing
}

private class IosNoOpGameSoundEffectPlayer : GameSoundEffectPlayer {
    override fun play(soundEffect: GameSoundEffect) = Unit
}

@Serializable
private data class StoredHistoryRecord(
    val gameId: String,
    val gameScore: Int,
    val gameLevel: Int,
    val gameDifficulty: String,
    val gameCategory: String,
    val gameSummary: Boolean,
    val gamePlayedTime: String,
    val gamePlayedDate: String,
) {
    fun toDomain(): HistoryRecord {
        val difficulty = runCatching { GameDifficulty.valueOf(gameDifficulty) }
            .getOrDefault(GameDifficulty.EASY)
        val category = runCatching { GameCategory.valueOf(gameCategory) }
            .getOrDefault(GameCategory.COUNTRIES)
        return HistoryRecord(
            gameId = gameId,
            gameScore = gameScore,
            gameLevel = gameLevel,
            gameDifficulty = difficulty,
            gameCategory = category,
            gameSummary = gameSummary,
            gamePlayedTime = gamePlayedTime,
            gamePlayedDate = gamePlayedDate,
        )
    }

    companion object {
        fun fromDomain(value: HistoryRecord): StoredHistoryRecord {
            return StoredHistoryRecord(
                gameId = value.gameId,
                gameScore = value.gameScore,
                gameLevel = value.gameLevel,
                gameDifficulty = value.gameDifficulty.name,
                gameCategory = value.gameCategory.name,
                gameSummary = value.gameSummary,
                gamePlayedTime = value.gamePlayedTime,
                gamePlayedDate = value.gamePlayedDate,
            )
        }
    }
}

@Serializable
private data class StoredSettings(
    val gameDifficulty: String = GameDifficulty.EASY.name,
    val gameCategory: String = GameCategory.COUNTRIES.name,
)

private fun dateNow(): String {
    val formatter = NSDateFormatter()
    formatter.dateStyle = 2u
    formatter.timeStyle = 0u
    return formatter.stringFromDate(NSDate())
}

private fun timeNow(): String {
    val formatter = NSDateFormatter()
    formatter.dateStyle = 0u
    formatter.timeStyle = 2u
    return formatter.stringFromDate(NSDate())
}
