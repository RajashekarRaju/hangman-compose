package com.developersbreach.hangman.repository

import androidx.lifecycle.LiveData
import com.developersbreach.hangman.repository.database.GameDatabase
import com.developersbreach.hangman.repository.database.entity.HistoryEntity
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.Words
import com.developersbreach.game.core.getFilteredWordsByGameDifficulty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Instance is created by Koin.
 */
class GameRepository(
    private val database: GameDatabase
) {

    /**
     * Returns the list of country name guessing words by difficulty.
     */
    fun getRandomGuessingWord(
        gameDifficulty: GameDifficulty,
        gameCategory: GameCategory
    ): List<Words> {
        return getFilteredWordsByGameDifficulty(gameDifficulty, gameCategory)
    }

    /**
     * Saves the single game history to database.
     */
    suspend fun saveCurrentGameToHistory(
        historyEntity: HistoryEntity
    ) {
        withContext(Dispatchers.IO) {
            database.historyDao.saveNewGameToHistory(historyEntity)
        }
    }

    /**
     * Returns the completed game history.
     */
    fun getCompleteGameHistory(): LiveData<List<HistoryEntity>> {
        return database.historyDao.getCompleteGameHistory()
    }

    /**
     * Get maximum highest number from the game score column.
     */
    fun getHighestScore(): LiveData<Int?> {
        return database.historyDao.getHighestScoreFromHistory()
    }

    /**
     * Delete the single game history item.
     */
    suspend fun deleteSelectedSingleGameHistory(
        historyEntity: HistoryEntity
    ) {
        withContext(Dispatchers.IO) {
            database.historyDao.deleteSingleGameHistory(historyEntity)
        }
    }

    /**
     * Deletes the completed game history.
     */
    suspend fun deleteCompleteGamesHistory() {
        withContext(Dispatchers.IO) {
            database.historyDao.deleteAllGamesHistory()
        }
    }
}