package com.developersbreach.hangman.utils

class GamePref(private val settings: PlatformSettings) {

    fun getGameDifficultyPref(): GameDifficulty {
        val difficulty = settings.getString(PREFERENCE_RESULT_GAME_DIFFICULTY, defaultDifficulty)
        return GameDifficulty.valueOf(difficulty ?: defaultDifficulty)
    }

    fun getGameCategoryPref(): GameCategory {
        val category = settings.getInt(PREFERENCE_RESULT_GAME_CATEGORY, defaultCategory.ordinal)
        return GameCategory.entries.find { it.ordinal == category } ?: defaultCategory
    }

    fun updateGameDifficultyPref(gameDifficulty: GameDifficulty) {
        settings.putString(PREFERENCE_RESULT_GAME_DIFFICULTY, gameDifficulty.name)
    }

    fun updateGameCategoryPref(gameCategory: Int) {
        settings.putInt(PREFERENCE_RESULT_GAME_CATEGORY, gameCategory)
    }

    companion object {
        private const val PREFERENCE_RESULT_GAME_DIFFICULTY = "game_difficulty_result"
        private const val PREFERENCE_RESULT_GAME_CATEGORY = "game_category_result"

        private val defaultDifficulty = GameDifficulty.EASY.name
        private val defaultCategory = GameCategory.COUNTRIES
    }
}
