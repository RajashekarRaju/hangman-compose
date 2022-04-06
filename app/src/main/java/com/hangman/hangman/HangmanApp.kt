package com.hangman.hangman

import android.app.Application
import com.hangman.hangman.repository.GameRepository
import com.hangman.hangman.repository.database.getDatabaseInstance
import timber.log.Timber

class HangmanApp : Application() {

    lateinit var repository: GameRepository

    override fun onCreate() {
        super.onCreate()

        val databaseInstance = getDatabaseInstance(applicationContext)
        repository = GameRepository(databaseInstance)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}