package com.developersbreach.hangman.repository

import com.developersbreach.game.core.HintType
import com.developersbreach.hangman.repository.database.GameDatabase
import com.developersbreach.hangman.repository.database.entity.HistoryEntity
import com.developersbreach.hangman.repository.metadata.generateHistoryMetadata
import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest
import com.developersbreach.hangman.repository.model.HistoryRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GameRepository(
    private val database: GameDatabase,
) : HistoryRepository, GameSessionRepository {

    override suspend fun saveCompletedGame(request: GameHistoryWriteRequest) {
        val metadata = generateHistoryMetadata()
        val historyEntity = HistoryEntity(
            gameId = metadata.gameId,
            gameScore = request.gameScore,
            gameLevel = request.gameLevel,
            gameSummary = request.gameSummary,
            gameDifficulty = request.gameDifficulty,
            gameCategory = request.gameCategory,
            gamePlayedTime = metadata.gamePlayedTime,
            gamePlayedDate = metadata.gamePlayedDate,
            hintsUsed = request.hintsUsed,
            hintTypesUsed = request.hintTypesUsed.joinToString(separator = ",") { it.name },
        )

        withContext(Dispatchers.IO) {
            database.historyDao.saveNewGameToHistory(historyEntity)
        }
    }

    override fun observeHistory(): Flow<List<HistoryRecord>> {
        return database.historyDao.getCompleteGameHistory().map { historyList ->
            historyList.map { history ->
                history.toRecord()
            }
        }
    }

    override suspend fun deleteHistoryItem(history: HistoryRecord) {
        withContext(Dispatchers.IO) {
            database.historyDao.deleteSingleGameHistory(history.toEntity())
        }
    }

    override suspend fun deleteAllHistory() {
        withContext(Dispatchers.IO) {
            database.historyDao.deleteAllGamesHistory()
        }
    }
}

private fun HistoryEntity.toRecord(): HistoryRecord {
    return HistoryRecord(
        gameId = gameId,
        gameScore = gameScore,
        gameLevel = gameLevel,
        gameDifficulty = gameDifficulty,
        gameCategory = gameCategory,
        gameSummary = gameSummary,
        gamePlayedTime = gamePlayedTime,
        gamePlayedDate = gamePlayedDate,
        hintsUsed = hintsUsed,
        hintTypesUsed = hintTypesUsed
            .split(",")
            .mapNotNull { value ->
                runCatching { HintType.valueOf(value) }.getOrNull()
            },
    )
}

private fun HistoryRecord.toEntity(): HistoryEntity {
    return HistoryEntity(
        gameId = gameId,
        gameScore = gameScore,
        gameLevel = gameLevel,
        gameDifficulty = gameDifficulty,
        gameCategory = gameCategory,
        gameSummary = gameSummary,
        gamePlayedTime = gamePlayedTime,
        gamePlayedDate = gamePlayedDate,
        hintsUsed = hintsUsed,
        hintTypesUsed = hintTypesUsed.joinToString(separator = ",") { it.name },
    )
}
