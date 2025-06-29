package com.developersbreach.hangman.repository

import androidx.lifecycle.LiveData
import com.developersbreach.hangman.repository.database.entity.HistoryEntity
import com.developersbreach.hangman.utils.GameCategory
import com.developersbreach.hangman.utils.GameDifficulty
import com.developersbreach.hangman.utils.Words
import com.developersbreach.hangman.utils.getFilteredWordsByGameDifficulty

class GameRepository(
    private val storage: HistoryStorage
) {
    fun getRandomGuessingWord(
        gameDifficulty: GameDifficulty,
        gameCategory: GameCategory
    ): List<Words> =
        getFilteredWordsByGameDifficulty(gameDifficulty, gameCategory)

    suspend fun saveCurrentGameToHistory(historyEntity: HistoryEntity) =
        storage.saveNewGameToHistory(historyEntity)

    fun getCompleteGameHistory(): LiveData<List<HistoryEntity>> =
        storage.getCompleteGameHistory()

    fun getHighestScore(): LiveData<Int?> =
        storage.getHighestScore()

    suspend fun deleteSelectedSingleGameHistory(historyEntity: HistoryEntity) =
        storage.deleteSingleGameHistory(historyEntity)

    suspend fun deleteCompleteGamesHistory() =
        storage.deleteAllGamesHistory()
}
