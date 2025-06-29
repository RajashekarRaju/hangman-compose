package com.developersbreach.hangman.diKoin

import com.developersbreach.hangman.repository.AndroidHistoryStorage
import com.developersbreach.hangman.repository.HistoryStorage
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.repository.database.getDatabaseInstance
import com.developersbreach.hangman.utils.GamePref
import com.developersbreach.hangman.utils.PlatformAudioPlayer
import com.developersbreach.hangman.utils.PlatformSettings
import com.developersbreach.hangman.viewmodel.GameViewModel
import com.developersbreach.hangman.viewmodel.HistoryViewModel
import com.developersbreach.hangman.viewmodel.OnBoardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        OnBoardingViewModel(
            repository = get(),
            gamePreferences = GamePref(get()),
            audioPlayer = get()
        )
    }
    viewModel { GameViewModel(get(), GamePref(get()), get()) }
    viewModel { HistoryViewModel(get()) }
}

val platformModule = module {
    single { PlatformSettings(androidContext()) }
    single { PlatformAudioPlayer(androidContext()) }
}

val databaseModule = module {
    single { getDatabaseInstance(get()) }
    single { AndroidHistoryStorage(get()) as HistoryStorage }
}

val repositoryModule = module {
    single { GameRepository(get()) }
}
