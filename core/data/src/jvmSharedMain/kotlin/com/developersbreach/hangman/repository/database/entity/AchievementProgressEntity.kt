package com.developersbreach.hangman.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievement_progress_table")
data class AchievementProgressEntity(

    @PrimaryKey
    @ColumnInfo(name = "column_achievement_id")
    val achievementId: String,

    @ColumnInfo(name = "column_is_unlocked")
    val isUnlocked: Boolean,

    @ColumnInfo(name = "column_is_unread")
    val isUnread: Boolean,

    @ColumnInfo(name = "column_unlocked_at_epoch_millis")
    val unlockedAtEpochMillis: Long?,

    @ColumnInfo(name = "column_progress_current")
    val progressCurrent: Int,

    @ColumnInfo(name = "column_progress_target")
    val progressTarget: Int,
)
