package com.developersbreach.hangman.diKoin

import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.repository.database.getDatabaseInstance
import com.developersbreach.hangman.ui.game.GameViewModel
import com.developersbreach.hangman.ui.history.HistoryViewModel
import com.developersbreach.hangman.ui.onboarding.OnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        OnBoardingViewModel(get(), get())
    }
    viewModel {
        GameViewModel(get(), get())
    }
    viewModel {
        HistoryViewModel(get())
    }
}

val databaseModule = module {
    single { getDatabaseInstance(get()) }
}

val repositoryModule = module {
    single { GameRepository(get()) }
}