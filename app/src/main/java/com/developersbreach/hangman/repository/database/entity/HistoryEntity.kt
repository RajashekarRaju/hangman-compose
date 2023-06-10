package com.developersbreach.hangman.repository.database.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.developersbreach.hangman.utils.GameCategory
import com.developersbreach.hangman.utils.GameDifficulty

@Entity(tableName = "history_table")
@Immutable
data class HistoryEntity(

    @Stable
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
    val gamePlayedDate: String
)