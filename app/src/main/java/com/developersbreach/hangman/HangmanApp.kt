package com.developersbreach.hangman

import android.app.Application
import com.developersbreach.hangman.composeapp.initKoinComponents
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class HangmanApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@HangmanApp)
            modules(initKoinComponents())
        }
    }
}