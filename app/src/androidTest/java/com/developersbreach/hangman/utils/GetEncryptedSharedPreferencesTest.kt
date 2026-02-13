package com.developersbreach.hangman.utils

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.developersbreach.game.core.GameDifficulty
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GetEncryptedSharedPreferencesTest {

    private lateinit var context: Context
    private lateinit var getEncryptedSharedPreferences: GetEncryptedSharedPreferences

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        getEncryptedSharedPreferences = GetEncryptedSharedPreferences(context)
    }

    @Test
    fun canStoreAndRetrieveData() {
        val prefs = getEncryptedSharedPreferences("preference_game_key")

        // Put a string value
        prefs.edit().putString("game_difficulty_result", GameDifficulty.HARD.name).apply()

        // Retrieve it
        val result = prefs.getString("game_difficulty_result", null)
        assertEquals(GameDifficulty.HARD.name, result)
    }

    @Test
    fun masterKeyIsCreatedAutomaticallyByLibrary() {
        //  the library calls MasterKey.Builder behind the scenes.)
        val prefs = getEncryptedSharedPreferences("preference_game_key")

        prefs.edit().putString("game_difficulty_result", GameDifficulty.MEDIUM.name).apply()
        assertEquals("MEDIUM", prefs.getString("game_difficulty_result", null))
    }
}