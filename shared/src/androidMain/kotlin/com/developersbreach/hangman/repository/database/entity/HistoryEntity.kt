package com.developersbreach.hangman.repository.database.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.developersbreach.hangman.utils.GameCategory
import com.developersbreach.hangman.utils.GameDifficulty

@Immutable
@Entity(tableName = "history_table")
actual data class HistoryEntity(
    @Stable
    @PrimaryKey
    @ColumnInfo(name = "column_game_id")
    actual val gameId: String,
    @ColumnInfo(name = "column_game_score")
    actual val gameScore: Int,
    @ColumnInfo(name = "column_game_level")
    actual val gameLevel: Int,
    @ColumnInfo(name = "column_game_difficulty")
    actual val gameDifficulty: GameDifficulty,
    @ColumnInfo(name = "column_game_category")
    actual val gameCategory: GameCategory,
    @ColumnInfo(name = "column_game_summary")
    actual val gameSummary: Boolean,
    @ColumnInfo(name = "column_game_time")
    actual val gamePlayedTime: String,
    @ColumnInfo(name = "column_game_date")
    actual val gamePlayedDate: String
)
