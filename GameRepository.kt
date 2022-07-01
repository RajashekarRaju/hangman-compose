package com.hangman.hangman.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hangman.hangman.repository.database.entity.HistoryEntity
import com.hangman.hangman.repository.database.GameDatabase
import com.hangman.hangman.utils.GameCategory
import com.hangman.hangman.utils.GameDifficulty
import com.hangman.hangman.utils.Words
import com.hangman.hangman.utils.getFilteredWordsByGameDifficulty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
    fun getCompleteGameHistory(): Flow<List<HistoryEntity>> {
        return database.historyDao.getCompleteGameHistory()
    }

    /**
     * Get maximum highest number from the game score column.
     */
    fun getHighestScore(): Flow<Int?> {
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