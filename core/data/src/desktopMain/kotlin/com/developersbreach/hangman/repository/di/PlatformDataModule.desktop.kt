package com.developersbreach.hangman.repository.di

import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.audio.GameSoundEffect
import com.developersbreach.hangman.audio.GameSoundEffectPlayer
import com.developersbreach.hangman.repository.GameRepository
import com.developersbreach.hangman.repository.GameSessionRepository
import com.developersbreach.hangman.repository.GameSettingsRepository
import com.developersbreach.hangman.repository.HistoryRepository
import com.developersbreach.hangman.repository.RoomGameSettingsRepository
import com.developersbreach.hangman.repository.database.GameDatabase
import com.developersbreach.hangman.repository.database.getDatabaseInstance
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDataModule(): Module = module {
    single { getDatabaseInstance() }
    single { get<GameDatabase>().gameSettingsDao }

    single { GameRepository(get()) }
    single<HistoryRepository> { get<GameRepository>() }
    single<GameSessionRepository> { get<GameRepository>() }
    single<GameSettingsRepository> { RoomGameSettingsRepository(get()) }
    single<BackgroundAudioController> { DesktopNoOpBackgroundAudioController() }
    single<GameSoundEffectPlayer> { DesktopNoOpGameSoundEffectPlayer() }
}

private class DesktopNoOpBackgroundAudioController : BackgroundAudioController {
    private var playing = false

    override fun playLoop() {
        playing = true
    }

    override fun stop() {
        playing = false
    }

    override fun isPlaying(): Boolean = playing
}

private class DesktopNoOpGameSoundEffectPlayer : GameSoundEffectPlayer {
    override fun play(soundEffect: GameSoundEffect) = Unit
}
