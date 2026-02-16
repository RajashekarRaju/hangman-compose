package com.developersbreach.hangman.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

@Entity(tableName = "history_table")
data class HistoryEntity(

    @PrimaryKey
    @ColumnInfo(name = "column_game_id")
    val gameId: String,

    @ColumnInfo(name = "column_game_score")
    val gameScore: Int,

    @ColumnInfo(name = "column_game_level")
    val gameLevel: Int,

    @ColumnInfo(name = "column_game_difficulty")
    val gameDifficulty: GameDifficulty,

    @ColumnInfo(name = "column_game_category")
    val gameCategory: GameCategory,

    @ColumnInfo(name = "column_game_summary")
    val gameSummary: Boolean,

    @ColumnInfo(name = "column_game_time")
    val gamePlayedTime: String,

    @ColumnInfo(name = "column_game_date")
    val gamePlayedDate: String,
)
