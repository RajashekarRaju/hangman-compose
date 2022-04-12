package com.hangman.hangman.repository

import com.hangman.hangman.repository.database.entity.HistoryEntity
import com.hangman.hangman.repository.database.entity.WordsEntity
import com.hangman.hangman.repository.database.GameDatabase
import com.hangman.hangman.utils.GameDifficulty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Instance is created by Koin.
 */
class GameRepository(
    private val database: GameDatabase
) {

    /**
     * Returns the list of country name guessing words from database by difficulty.
     */
    suspend fun getRandomGuessingWord(
        gameDifficulty: GameDifficulty
    ): List<WordsEntity> {
        val guessingWords: List<WordsEntity>
        withContext(Dispatchers.IO) {
            guessingWords = database.wordsDao.getGuessingWordsByGameDifficulty(gameDifficulty)
        }

        return guessingWords
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
    suspend fun getCompleteGameHistory(): List<HistoryEntity> {
        val gameHistoryEntityList: List<HistoryEntity>
        withContext(Dispatchers.IO) {
            gameHistoryEntityList = database.historyDao.getCompleteGameHistory()
        }

        return gameHistoryEntityList
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