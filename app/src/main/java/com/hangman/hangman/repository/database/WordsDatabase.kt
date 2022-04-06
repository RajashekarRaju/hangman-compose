package com.hangman.hangman.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hangman.hangman.repository.database.entity.HistoryEntity
import com.hangman.hangman.repository.database.entity.WordsEntity
import com.hangman.hangman.repository.database.dao.HistoryDao
import com.hangman.hangman.repository.database.dao.WordsDao

@Database(entities = [HistoryEntity::class, WordsEntity::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {
    abstract val historyDao: HistoryDao
    abstract val wordsDao: WordsDao
}

private lateinit var INSTANCE: GameDatabase

private const val DATABASE_NAME = "hangman database"

fun getDatabaseInstance(
    context: Context
): GameDatabase {
    synchronized(GameDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                GameDatabase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}