package com.hangman.hangman.repository.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.hangman.hangman.repository.database.entity.WordsEntity
import com.hangman.hangman.utils.GameDifficulty
import com.hangman.hangman.utils.getFilteredWordsByGameDifficulty

@Dao
interface WordsDao {

    @Query("SELECT * FROM words_table")
    suspend fun getGuessingWordsByGameDifficulty(
        gameDifficulty: GameDifficulty
    ): List<WordsEntity> {
        return getFilteredWordsByGameDifficulty(gameDifficulty)
    }
}