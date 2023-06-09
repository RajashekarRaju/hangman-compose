@file:Suppress("unused")

package com.hangman.hangman

import android.app.Application
import com.hangman.hangman.diKoin.databaseModule
import com.hangman.hangman.diKoin.repositoryModule
import com.hangman.hangman.diKoin.viewModelModule
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