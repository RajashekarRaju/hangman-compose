package com.developersbreach.hangman.repository

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.Words
import com.developersbreach.game.core.getFilteredWordsByGameDifficulty
import com.developersbreach.hangman.repository.database.GameDatabase
import com.developersbreach.hangman.repository.database.entity.HistoryEntity
import com.developersbreach.hangman.repository.model.GameHistoryWriteRequest
import com.developersbreach.hangman.repository.model.HistoryRecord
import com.developersbreach.hangman.utils.getDateAndTime
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Instance is created by Koin.
 */
class GameRepository(
    private val database: GameDatabase,
) : HistoryRepository, GameSessionRepository {

    override fun getRandomGuessingWord(
        gameDifficulty: GameDifficulty,
        gameCategory: GameCategory,
    ): List<Words> {
        return getFilteredWordsByGameDifficulty(gameDifficulty, gameCategory)
    }

    override suspend fun saveCompletedGame(request: GameHistoryWriteRequest) {
        val (date, time) = getDateAndTime()
        val historyEntity = HistoryEntity(
            gameId = UUID.randomUUID().toString(),
            gameScore = request.gameScore,
            gameLevel = request.gameLevel,
            gameSummary = request.gameSummary,
            gameDifficulty = request.gameDifficulty,
            gameCategory = request.gameCategory,
            gamePlayedTime = time,
            gamePlayedDate = date,
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
    )
}
