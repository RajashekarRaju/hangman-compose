package com.developersbreach.hangman.repository.di

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.audio.GameSoundEffect
import com.developersbreach.hangman.audio.GameSoundEffectPlayer
import com.developersbreach.hangman.repository.GameSessionRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.HistoryRepository
import com.developersbreach.hangman.repository.metadata.generateHistoryMetadata
import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest
import com.developersbreach.hangman.repository.model.HistoryRecord
import com.developersbreach.hangman.repository.storage.StoredHistoryRecord
import com.developersbreach.hangman.repository.storage.StoredSettings
import com.developersbreach.hangman.repository.storage.toDomain
import com.developersbreach.hangman.repository.storage.toGameCategory
import com.developersbreach.hangman.repository.storage.toGameDifficulty
import com.developersbreach.hangman.repository.storage.toStored
import kotlinx.browser.window
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDataModule(): Module = module {
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

    override suspend fun getGameDifficulty(): GameDifficulty = settings.gameDifficulty.toGameDifficulty()

    override suspend fun getGameCategory(): GameCategory = settings.gameCategory.toGameCategory()

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