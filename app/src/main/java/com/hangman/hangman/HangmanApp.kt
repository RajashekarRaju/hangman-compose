package com.hangman.hangman

import android.app.Application
import com.hangman.hangman.repository.WordsRepository
import timber.log.Timber

class HangmanApp : Application() {

    lateinit var repository: WordsRepository

    override fun onCreate() {
        super.onCreate()

        repository = WordsRepository()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}