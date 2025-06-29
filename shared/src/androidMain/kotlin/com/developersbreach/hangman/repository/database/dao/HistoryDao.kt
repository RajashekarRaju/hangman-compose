package com.developersbreach.hangman.repository.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developersbreach.hangman.repository.database.entity.HistoryEntity

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNewGameToHistory(historyEntity: HistoryEntity)

    @Query("SELECT * FROM history_table")
    fun getCompleteGameHistory(): LiveData<List<HistoryEntity>>

    @Query("SELECT MAX(column_game_score) FROM history_table")
    fun getHighestScoreFromHistory(): LiveData<Int?>

    @Delete
    fun deleteSingleGameHistory(historyEntity: HistoryEntity)

    @Query("DELETE FROM history_table")
    fun deleteAllGamesHistory()
}