package com.developersbreach.hangman.repository.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.developersbreach.hangman.repository.database.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNewGameToHistory(historyEntity: HistoryEntity)

    @Query("SELECT * FROM history_table")
    fun getCompleteGameHistory(): Flow<List<HistoryEntity>>

    @Delete
    fun deleteSingleGameHistory(historyEntity: HistoryEntity)

    @Query("DELETE FROM history_table")
    fun deleteAllGamesHistory()
}
