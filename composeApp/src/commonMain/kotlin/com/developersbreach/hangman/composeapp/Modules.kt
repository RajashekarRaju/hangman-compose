package com.developersbreach.hangman.composeapp

import com.developersbreach.hangman.ui.game.GameViewModel
import com.developersbreach.hangman.ui.history.HistoryViewModel
import com.developersbreach.hangman.ui.onboarding.OnBoardingViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

expect fun platformRepositoryModule(): Module

fun initKoinComponents(): List<Module> {
    return listOf(
        platformRepositoryModule(),
        viewModelModule
    )
}

private val viewModelModule = module {
    viewModel {
        OnBoardingViewModel(get(), get(), get())
    }
    viewModel {
        GameViewModel(get(), get(), get())
    }
    viewModel {
        HistoryViewModel(get())
    }
}