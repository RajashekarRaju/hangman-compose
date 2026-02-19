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
import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.js.JsName
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformRepositoryModule(): Module = module {
    single { WasmLocalStorageGameRepository() }
    single<HistoryRepository> { get<WasmLocalStorageGameRepository>() }
    single<GameSessionRepository> { get<WasmLocalStorageGameRepository>() }
    single<GameSettingsRepository> { WasmLocalStorageGameSettingsRepository() }
    single<BackgroundAudioController> { WasmNoOpBackgroundAudioController() }
    single<GameSoundEffectPlayer> { WasmNoOpGameSoundEffectPlayer() }
}

private const val HISTORY_KEY = "hangman.history.v1"
private const val SETTINGS_KEY = "hangman.settings.v1"

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
        val payload = json.encodeToString(history.map { StoredHistoryRecord.fromDomain(it) })
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
        val record = HistoryRecord(
            gameId = "web-${jsTimestamp()}-${historyState.value.size + 1}",
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

private class WasmLocalStorageGameSettingsRepository : GameSettingsRepository {
    private var settings: StoredSettings = loadSettings()

    private fun loadSettings(): StoredSettings {
        val raw = window.localStorage.getItem(SETTINGS_KEY) ?: return StoredSettings()
        return runCatching { json.decodeFromString<StoredSettings>(raw) }.getOrDefault(StoredSettings())
    }

    private fun persist() {
        window.localStorage.setItem(SETTINGS_KEY, json.encodeToString(settings))
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

private class WasmNoOpBackgroundAudioController : BackgroundAudioController {
    private var playing = false

    override fun playLoop() {
        playing = true
    }

    override fun stop() {
        playing = false
    }

    override fun isPlaying(): Boolean = playing
}

private class WasmNoOpGameSoundEffectPlayer : GameSoundEffectPlayer {
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

@JsName("Date")
private external class JsDate {
    constructor()

    fun toLocaleDateString(): String
    fun toLocaleTimeString(): String

    companion object {
        fun now(): Double
    }
}

private fun jsTimestamp(): String = JsDate.now().toLong().toString()

private fun dateNow(): String = JsDate().toLocaleDateString()

private fun timeNow(): String = JsDate().toLocaleTimeString()
