package com.developersbreach.hangman

import android.app.Application
import com.developersbreach.hangman.diKoin.databaseModule
import com.developersbreach.hangman.diKoin.repositoryModule
import com.developersbreach.hangman.diKoin.viewModelModule
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
            modules(
                databaseModule,
                repositoryModule,
                viewModelModule,
            )
        }
    }
}