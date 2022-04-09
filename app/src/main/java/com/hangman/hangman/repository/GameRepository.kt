package com.hangman.hangman.repository

import com.hangman.hangman.repository.database.entity.HistoryEntity
import com.hangman.hangman.repository.database.entity.WordsEntity
import com.hangman.hangman.repository.database.GameDatabase
import com.hangman.hangman.utils.GameDifficulty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepository(
    private val database: GameDatabase
) {

    suspend fun getRandomGuessingWord(
        gameDifficulty: GameDifficulty
    ): List<WordsEntity> {
        val guessingWords: List<WordsEntity>
        withContext(Dispatchers.IO) {
            guessingWords = database.wordsDao.getGuessingWordsByGameDifficulty(gameDifficulty)
        }

        return guessingWords
    }

    suspend fun saveCurrentGameToHistory(
        historyEntity: HistoryEntity
    ) {
        withContext(Dispatchers.IO) {
            database.historyDao.saveNewGameToHistory(historyEntity)
        }
    }

    suspend fun getCompleteGameHistory(): List<HistoryEntity> {
        val gameHistoryEntityList: List<HistoryEntity>
        withContext(Dispatchers.IO) {
            gameHistoryEntityList = database.historyDao.getCompleteGameHistory()
        }

        return gameHistoryEntityList
    }

    suspend fun deleteSelectedSingleGameHistory(
        historyEntity: HistoryEntity
    ) {
        withContext(Dispatchers.IO) {
            database.historyDao.deleteSingleGameHistory(historyEntity)
        }
    }

    suspend fun deleteCompleteGamesHistory() {
        withContext(Dispatchers.IO) {
            database.historyDao.deleteAllGamesHistory()
        }
    }
}