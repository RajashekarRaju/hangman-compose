package com.developersbreach.hangman.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.developersbreach.hangman.repository.database.dao.HistoryDao
import com.developersbreach.hangman.repository.database.entity.HistoryEntity

private const val DATABASE_NAME = "hangman database"

/**
 * To persist all the game history into room.
 */
@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {
    abstract val historyDao: HistoryDao
}

/**
 * Single database instance will be created by koin.
 */
fun getDatabaseInstance(
    context: Context
): GameDatabase {
    return Room.databaseBuilder(
        context.applicationContext,
        GameDatabase::class.java,
        DATABASE_NAME
    )
        .fallbackToDestructiveMigration()
        .build()
}