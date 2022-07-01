package com.hangman.hangman.repository.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hangman.hangman.repository.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNewGameToHistory(historyEntity: HistoryEntity)

    @Query("SELECT * FROM history_table")
    fun getCompleteGameHistory(): Flow<List<HistoryEntity>>

    @Query("SELECT MAX(column_game_score) FROM history_table")
    fun getHighestScoreFromHistory(): Flow<Int?>

    @Delete
    fun deleteSingleGameHistory(historyEntity: HistoryEntity)

    @Query("DELETE FROM history_table")
    fun deleteAllGamesHistory()
}