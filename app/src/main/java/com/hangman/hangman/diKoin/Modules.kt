package com.hangman.hangman.diKoin

import com.hangman.hangman.repository.GameRepository
import com.hangman.hangman.repository.database.getDatabaseInstance
import com.hangman.hangman.ui.game.GameViewModel
import com.hangman.hangman.ui.history.HistoryViewModel
import com.hangman.hangman.ui.onboarding.OnBoardingViewModel
import com.hangman.hangman.utils.GameDifficultyPref
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

val useCase = module {
    single { GameDifficultyPref(get()) }
}