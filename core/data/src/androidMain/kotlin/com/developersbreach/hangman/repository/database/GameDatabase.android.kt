package com.developersbreach.hangman.repository.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<GameDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<GameDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}

fun getDatabaseInstance(context: Context): GameDatabase {
    return buildDatabase(getDatabaseBuilder(context))
}
