package com.developersbreach.hangman.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GamePrefTest {

    private lateinit var context: Context
    private lateinit var gamePref: GamePref

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        gamePref = GamePref(context)
    }

    @Test
    fun defaultDifficultyShouldBeEasy() {
        // No updates yet, the default should be EASY
        val actualDifficulty = gamePref.getGameDifficultyPref()

        assertEquals(GameDifficulty.EASY, actualDifficulty)
    }

    @Test
    fun canStoreAndRetrieveGameDifficulty() {
        // Update and verify preference difficulty to HARD
        gamePref.updateGameDifficultyPref(GameDifficulty.HARD)

        val storedDifficulty = gamePref.getGameDifficultyPref()

        assertEquals(GameDifficulty.HARD, storedDifficulty)
    }

    @Test
    fun updateDifficultyMultipleTimes() {
        // Start and verify with MEDIUM
        gamePref.updateGameDifficultyPref(GameDifficulty.MEDIUM)
        var currentDiff = gamePref.getGameDifficultyPref()
        assertEquals(GameDifficulty.MEDIUM, currentDiff)

        // Update and verify preference to HARD
        gamePref.updateGameDifficultyPref(GameDifficulty.HARD)
        currentDiff = gamePref.getGameDifficultyPref()
        assertEquals(GameDifficulty.HARD, currentDiff)

        // Update and verify preference set EASY
        gamePref.updateGameDifficultyPref(GameDifficulty.EASY)
        currentDiff = gamePref.getGameDifficultyPref()
        assertEquals(GameDifficulty.EASY, currentDiff)
    }

    @Test
    fun defaultCategoryShouldBeCountries() {
        // No updates, verify the default category is COUNTRIES
        val actualCategory = gamePref.getGameCategoryPref()

        assertEquals(GameCategory.COUNTRIES, actualCategory)
    }

    @Test
    fun canStoreAndRetrieveGameCategory() {
        // (A) Suppose category ordinal 2 => e.g. GameCategory with index 2
        gamePref.updateGameCategoryPref(2)

        // (B) Read it back
        val storedCategory = gamePref.getGameCategoryPref()
        // (C) Confirm it matches the category with ordinal 2
        // (actual category depends on your enum definition, so adjust as needed)
        assertEquals(2, storedCategory.ordinal)
    }
}
