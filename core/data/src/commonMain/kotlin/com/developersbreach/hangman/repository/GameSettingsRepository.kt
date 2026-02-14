package com.developersbreach.hangman.repository

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

interface GameSettingsRepository {

    fun getGameDifficulty(): GameDifficulty

    fun getGameCategory(): GameCategory

    fun setGameDifficulty(gameDifficulty: GameDifficulty)

    fun setGameCategory(gameCategory: GameCategory)
}
