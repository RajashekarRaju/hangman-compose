package com.developersbreach.hangman.composeapp

import com.developersbreach.hangman.ui.achievements.AchievementsViewModel
import com.developersbreach.hangman.ui.game.GameViewModel
import com.developersbreach.hangman.ui.history.HistoryViewModel
import com.developersbreach.hangman.ui.mainmenu.MainMenuViewModel
import com.developersbreach.hangman.ui.settings.SettingsViewModel
import com.developersbreach.hangman.repository.di.platformDataModule
import com.developersbreach.hangman.ui.common.notification.AchievementNotificationCoordinator
import com.developersbreach.hangman.ui.common.notification.DefaultAchievementNotificationCoordinator
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
    single<AchievementNotificationCoordinator> {
        DefaultAchievementNotificationCoordinator()
    }
    viewModel {
        AppInitializerViewModel(get(), get())
    }
    viewModel {
        MainMenuViewModel(get(), get(), get(), get())
    }
    viewModel {
        SettingsViewModel(get(), get())
    }
    viewModel {
        GameViewModel(get(), get(), get(), get(), get())
    }
    viewModel {
        HistoryViewModel(get())
    }
    viewModel {
        AchievementsViewModel(get())
    }
}