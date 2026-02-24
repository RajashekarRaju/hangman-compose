package com.developersbreach.hangman.composeapp

import com.developersbreach.hangman.ui.game.GameViewModel
import com.developersbreach.hangman.ui.history.HistoryViewModel
import com.developersbreach.hangman.ui.onboarding.OnBoardingViewModel
import com.developersbreach.hangman.repository.di.platformDataModule
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

fun initKoinComponents(): List<Module> {
    return listOf(
        platformDataModule(),
        viewModelModule
    )
}

private val viewModelModule = module {
    viewModel {
        AppInitializerViewModel(get())
    }
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