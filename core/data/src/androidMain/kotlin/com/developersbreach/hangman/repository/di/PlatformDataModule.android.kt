package com.developersbreach.hangman.repository.di

import com.developersbreach.hangman.audio.AndroidBackgroundAudioController
import com.developersbreach.hangman.audio.AndroidGameSoundEffectPlayer
import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.audio.GameSoundEffectPlayer
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.repository.GameSessionRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.HistoryRepository
import com.developersbreach.hangman.repository.AchievementsRepository
import com.developersbreach.hangman.repository.RoomAchievementsRepository
import com.developersbreach.hangman.repository.RoomGameSettingsRepository
import com.developersbreach.hangman.repository.database.GameDatabase
import com.developersbreach.hangman.repository.database.getDatabaseInstance
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDataModule(): Module = module {
    single { getDatabaseInstance(get()) }
    single { get<GameDatabase>().gameSettingsDao }
    single { get<GameDatabase>().achievementsDao }

    single { GameRepository(get()) }
    single<AchievementsRepository> { RoomAchievementsRepository(get()) }
    single<HistoryRepository> { get<GameRepository>() }
    single<GameSessionRepository> { get<GameRepository>() }
    single<GameSettingsRepository> { RoomGameSettingsRepository(get()) }
    single<BackgroundAudioController> { AndroidBackgroundAudioController(get()) }
    single<GameSoundEffectPlayer> { AndroidGameSoundEffectPlayer(get()) }
}
