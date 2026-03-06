package com.developersbreach.hangman

import android.app.Application
import com.developersbreach.hangman.composeapp.initKoinComponents
import com.developersbreach.hangman.logging.LoggingInitializationConfig
import com.developersbreach.hangman.logging.LogLevel
import com.developersbreach.hangman.logging.initializeLogging
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class HangmanApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogging()
        initKoin()
    }

    private fun initLogging() {
        val isReleaseBuild = !BuildConfig.DEBUG
        initializeLogging(
            LoggingInitializationConfig(
                minLevel = if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.NONE,
                sentryDsn = BuildConfig.SENTRY_DSN,
                sentryEnabled = isReleaseBuild,
            ),
        )
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@HangmanApp)
            modules(initKoinComponents())
        }
    }
}