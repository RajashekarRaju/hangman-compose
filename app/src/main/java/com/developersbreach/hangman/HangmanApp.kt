package com.developersbreach.hangman

import android.app.Application
import com.developersbreach.hangman.composeapp.initKoinComponents
import com.developersbreach.hangman.logging.LogConfig
import com.developersbreach.hangman.logging.LogLevel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class HangmanApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogging()
        initKoin()
    }

    private fun initLogging() {
        LogConfig.minLevel = when {
            BuildConfig.DEBUG -> LogLevel.DEBUG
            else -> LogLevel.NONE
        }
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@HangmanApp)
            modules(initKoinComponents())
        }
    }
}