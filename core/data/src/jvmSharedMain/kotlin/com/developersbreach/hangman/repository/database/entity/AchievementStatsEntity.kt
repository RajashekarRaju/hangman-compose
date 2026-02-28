package com.developersbreach.hangman.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievement_stats_table")
data class AchievementStatsEntity(

    @PrimaryKey
    @ColumnInfo(name = "column_stats_id")
    val statsId: Int = STATS_SINGLETON_ID,

    @ColumnInfo(name = "column_games_played")
    val gamesPlayed: Int = 0,

    @ColumnInfo(name = "column_games_won")
    val gamesWon: Int = 0,

    @ColumnInfo(name = "column_games_lost")
    val gamesLost: Int = 0,

    @ColumnInfo(name = "column_history_entries_recorded_total")
    val historyEntriesRecordedTotal: Int = 0,

    @ColumnInfo(name = "column_current_win_streak")
    val currentWinStreak: Int = 0,

    @ColumnInfo(name = "column_best_win_streak")
    val bestWinStreak: Int = 0,

    @ColumnInfo(name = "column_total_hints_used")
    val totalHintsUsed: Int = 0,

    @ColumnInfo(name = "column_games_won_without_hints")
    val gamesWonWithoutHints: Int = 0,

    @ColumnInfo(name = "column_perfect_wins")
    val perfectWins: Int = 0,

    @ColumnInfo(name = "column_highest_score")
    val highestScore: Int = 0,

    @ColumnInfo(name = "column_wins_by_category")
    val winsByCategory: String = "{}",

    @ColumnInfo(name = "column_wins_by_difficulty")
    val winsByDifficulty: String = "{}",
) {
    companion object {
        const val STATS_SINGLETON_ID = 1
    }
}
