package com.developersbreach.hangman.diKoin

import com.developersbreach.hangman.audio.AndroidBackgroundAudioController
import com.developersbreach.hangman.audio.AndroidGameSoundEffectPlayer
import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.audio.GameSoundEffectPlayer
import com.developersbreach.hangman.repository.AndroidGameSettingsRepository
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.repository.GameSessionRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.HistoryRepository
import com.developersbreach.hangman.repository.database.getDatabaseInstance
import com.developersbreach.hangman.ui.game.GameViewModel
import com.developersbreach.hangman.ui.history.HistoryViewModel
import com.developersbreach.hangman.ui.onboarding.OnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
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

val databaseModule = module {
    single { getDatabaseInstance(get()) }
}

val repositoryModule = module {
    single { GameRepository(get()) }
    single<HistoryRepository> { get<GameRepository>() }
    single<GameSessionRepository> { get<GameRepository>() }
    single<GameSettingsRepository> { AndroidGameSettingsRepository(get()) }
    single<BackgroundAudioController> { AndroidBackgroundAudioController(get()) }
    single<GameSoundEffectPlayer> { AndroidGameSoundEffectPlayer(get()) }
}
