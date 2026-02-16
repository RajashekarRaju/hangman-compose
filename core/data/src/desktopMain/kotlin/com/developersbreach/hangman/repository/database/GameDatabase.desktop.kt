package com.developersbreach.hangman.repository.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

private fun getDatabaseBuilder(): RoomDatabase.Builder<GameDatabase> {
    val appDir = File(System.getProperty("user.home"), ".hangman-compose")
    appDir.mkdirs()
    val dbFile = File(appDir, DATABASE_NAME)
    return Room.databaseBuilder<GameDatabase>(
        name = dbFile.absolutePath,
    )
}

fun getDatabaseInstance(): GameDatabase {
    return buildDatabase(getDatabaseBuilder())
}
