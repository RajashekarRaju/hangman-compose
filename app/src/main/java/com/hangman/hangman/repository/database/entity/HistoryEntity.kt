package com.hangman.hangman.repository.database.entity

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history_table")
@Immutable
data class HistoryEntity(

    @Stable
    @PrimaryKey
    @ColumnInfo(name = "column_game_id")
    val gameId: Int,

    @ColumnInfo(name = "column_game_score")
    val gameScore: Int,

    @ColumnInfo(name = "column_game_difficulty")
    val gameDifficulty: Int,

    @ColumnInfo(name = "column_game_summary")
    val gameSummary: Boolean
)
