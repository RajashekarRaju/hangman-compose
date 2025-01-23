package com.developersbreach.hangman.utils

import android.content.Context

class GamePref(
    context: Context
) {
    private val sharedPref = GetEncryptedSharedPreferences(context).invoke(
        preferenceName = PREFERENCE_KEY_GAME_DIFFICULTY
    )

    fun getGameDifficultyPref(): GameDifficulty {
        val difficulty = sharedPref.getString(PREFERENCE_RESULT_GAME_DIFFICULTY, defaultDifficulty)
        return GameDifficulty.valueOf(difficulty ?: defaultDifficulty)
    }

    fun getGameCategoryPref(): GameCategory {
        val category = sharedPref.getInt(PREFERENCE_RESULT_GAME_CATEGORY, defaultCategory.ordinal)
        return GameCategory.entries.find { it.ordinal == category } ?: defaultCategory
    }

    fun updateGameDifficultyPref(
        gameDifficulty: GameDifficulty
    ) {
        sharedPref
            .edit()
            .putString(PREFERENCE_RESULT_GAME_DIFFICULTY, gameDifficulty.name)
            .apply()
    }

    fun updateGameCategoryPref(
        gameCategory: Int
    ) {
        sharedPref
            .edit()
            .putInt(PREFERENCE_RESULT_GAME_CATEGORY, gameCategory)
            .apply()
    }

    companion object {
        private const val PREFERENCE_KEY_GAME_DIFFICULTY = "preference_game_key"
        private const val PREFERENCE_RESULT_GAME_DIFFICULTY = "game_difficulty_result"
        private const val PREFERENCE_RESULT_GAME_CATEGORY = "game_category_result"

        private val defaultDifficulty = GameDifficulty.EASY.name
        private val defaultCategory = GameCategory.COUNTRIES
    }
}