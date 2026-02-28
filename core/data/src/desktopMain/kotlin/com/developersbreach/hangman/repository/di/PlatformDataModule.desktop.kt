package com.developersbreach.hangman.repository.di

import com.developersbreach.hangman.audio.BackgroundAudioController
import com.developersbreach.hangman.audio.GameSoundEffect
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
import java.io.BufferedInputStream
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDataModule(): Module = module {
    single { getDatabaseInstance() }
    single { get<GameDatabase>().gameSettingsDao }
    single { get<GameDatabase>().achievementsDao }

    single { GameRepository(get()) }
    single<AchievementsRepository> { RoomAchievementsRepository(get()) }
    single<HistoryRepository> { get<GameRepository>() }
    single<GameSessionRepository> { get<GameRepository>() }
    single<GameSettingsRepository> { RoomGameSettingsRepository(get()) }
    single<BackgroundAudioController> { DesktopBackgroundAudioController() }
    single<GameSoundEffectPlayer> { DesktopGameSoundEffectPlayer() }
}

private class DesktopBackgroundAudioController : BackgroundAudioController {
    private val backgroundClip = createClip("game_background_music.mp3")

    override fun playLoop() {
        val clip = backgroundClip ?: return
        if (clip.isRunning) return
        clip.framePosition = 0
        clip.loop(Clip.LOOP_CONTINUOUSLY)
        clip.start()
    }

    override fun stop() {
        backgroundClip?.stop()
        backgroundClip?.framePosition = 0
    }

    override fun isPlaying(): Boolean = backgroundClip?.isRunning == true
}

private class DesktopGameSoundEffectPlayer : GameSoundEffectPlayer {
    private val clips: Map<GameSoundEffect, Clip?> = GameSoundEffect.entries.associateWith { sound ->
        createClip("${sound.resourceKey}.mp3")
    }

    override fun play(soundEffect: GameSoundEffect) {
        val clip = clips[soundEffect] ?: return
        clip.stop()
        clip.framePosition = 0
        clip.start()
    }
}

private fun createClip(resourceName: String): Clip? {
    val resourcePathCandidates = listOf(
        "/$resourceName",
        "/raw/$resourceName",
    )

    for (resourcePath in resourcePathCandidates) {
        val inputStream = DesktopBackgroundAudioController::class.java.getResourceAsStream(resourcePath)
            ?: continue

        val clip = runCatching {
            AudioSystem.getClip().also { createdClip ->
                BufferedInputStream(inputStream).use { buffered ->
                    AudioSystem.getAudioInputStream(buffered).use { compressedInput ->
                        val baseFormat = compressedInput.format
                        val decodedFormat = AudioFormat(
                            AudioFormat.Encoding.PCM_SIGNED,
                            baseFormat.sampleRate,
                            16,
                            baseFormat.channels,
                            baseFormat.channels * 2,
                            baseFormat.sampleRate,
                            false,
                        )
                        AudioSystem.getAudioInputStream(decodedFormat, compressedInput).use { pcmInput ->
                            createdClip.open(pcmInput)
                        }
                    }
                }
            }
        }.onFailure { error ->
            System.err.println("Failed to load desktop audio resource '$resourcePath': ${error.message}")
        }.getOrNull()

        if (clip != null) return clip
    }

    System.err.println("Desktop audio resource not found: $resourceName")
    return null
}
