package com.hangman.hangman.repository.database.dao

import androidx.room.*
import com.hangman.hangman.repository.database.entity.HistoryEntity
import com.hangman.hangman.repository.gameHistory

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveNewGameToHistory(historyEntity: HistoryEntity)

    @Query("SELECT * FROM history_table")
    suspend fun getCompleteGameHistory(): List<HistoryEntity> = gameHistory

    @Delete
    suspend fun deleteSingleGameHistory(historyEntity: HistoryEntity)

    @Query("DELETE FROM history_table")
    suspend fun deleteAllGamesHistory()
}