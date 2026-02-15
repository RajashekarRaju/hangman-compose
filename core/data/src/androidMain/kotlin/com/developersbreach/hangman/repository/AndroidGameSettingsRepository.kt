package com.developersbreach.hangman.repository

import android.app.Application
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.utils.GamePref

class AndroidGameSettingsRepository(
    application: Application
) : GameSettingsRepository {

    private val gamePref = GamePref(application)

    override fun getGameDifficulty(): GameDifficulty {
        return gamePref.getGameDifficultyPref()
    }

    override fun getGameCategory(): GameCategory {
        return gamePref.getGameCategoryPref()
    }

    override fun setGameDifficulty(gameDifficulty: GameDifficulty) {
        gamePref.updateGameDifficultyPref(gameDifficulty)
    }

    override fun setGameCategory(gameCategory: GameCategory) {
        gamePref.updateGameCategoryPref(gameCategory.ordinal)
    }
}
