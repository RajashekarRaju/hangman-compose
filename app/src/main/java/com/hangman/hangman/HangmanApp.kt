@file:Suppress("unused")

package com.hangman.hangman

import android.app.Application
import com.hangman.hangman.diKoin.databaseModule
import com.hangman.hangman.diKoin.repositoryModule
import com.hangman.hangman.diKoin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber


class HangmanApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
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