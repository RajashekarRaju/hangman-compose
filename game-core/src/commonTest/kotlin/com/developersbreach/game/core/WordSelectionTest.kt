package com.developersbreach.game.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WordSelectionTest {

    @Test
    fun `easy medium and hard always return five words in expected range`() {
        GameCategory.entries.forEach { category ->
            assertWordsInRange(GameDifficulty.EASY, category)
            assertWordsInRange(GameDifficulty.MEDIUM, category)
            assertWordsInRange(GameDifficulty.HARD, category)
        }
    }

    @Test
    fun `very hard falls back to hard for sparse categories`() {
        val words = getFilteredWordsByGameDifficulty(GameDifficulty.VERY_HARD, GameCategory.COMPANIES)

        assertEquals(LEVELS_PER_GAME, words.size)
        assertTrue(
            words.all { it.wordName.playableLetterCount() in GameDifficulty.HARD.wordLengthRange() }
        )
    }

    private fun assertWordsInRange(
        difficulty: GameDifficulty,
        category: GameCategory,
    ) {
        val words = getFilteredWordsByGameDifficulty(difficulty, category)
        assertEquals(LEVELS_PER_GAME, words.size)
        assertTrue(words.all { it.wordName.playableLetterCount() in difficulty.wordLengthRange() })
    }
}
