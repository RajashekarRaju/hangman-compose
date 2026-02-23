package com.developersbreach.game.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameSessionEngineTest {

    @Test
    fun `initial snapshot preserves spaces and guesses start hidden`() {
        val engine = engineFor(words = listOf("sea turtle"), levels = 1)

        val snapshot = engine.snapshot()

        assertEquals("sea turtle", snapshot.currentWord)
        assertEquals(" ", snapshot.playerGuesses[3])
        assertEquals(10, snapshot.playerGuesses.size)
        assertEquals(8, snapshot.attemptsLeftToGuess)
        assertFalse(snapshot.gameOverByWinning)
        assertFalse(snapshot.gameOverByNoAttemptsLeft)
    }

    @Test
    fun `incorrect guess decrements attempts and eventually loses`() {
        val engine = engineFor(words = listOf("bear"), levels = 1)

        repeat(7) { index ->
            val update = engine.guessAlphabet(idFor("ZQXJVNM"[index]))
            assertFalse(update.gameLost)
        }

        val losingUpdate = engine.guessAlphabet(idFor('P'))
        assertTrue(losingUpdate.gameLost)
        assertTrue(losingUpdate.state.gameOverByNoAttemptsLeft)
        assertEquals(0, losingUpdate.state.attemptsLeftToGuess)
    }

    @Test
    fun `solving a level awards points and advances`() {
        val engine = engineFor(
            words = listOf("wolf", "otter"),
            levels = 2,
            hintsPerLevel = 1,
        )

        guessWord(engine, "wolf")
        val afterLevelOne = engine.snapshot()

        assertEquals(1, afterLevelOne.currentPlayerLevel)
        assertEquals("otter", afterLevelOne.currentWord)
        assertEquals("", afterLevelOne.playerGuesses.first())
        assertEquals(4, afterLevelOne.pointsScoredOverall)
        assertEquals(8, afterLevelOne.attemptsLeftToGuess)
        assertEquals(1, afterLevelOne.hintsRemaining)
    }

    @Test
    fun `reveal hint marks index and consumes hint`() {
        val engine = engineFor(words = listOf("llama"), levels = 1, hintsPerLevel = 1)

        val update = engine.applyHint(HintType.REVEAL_LETTER)

        assertTrue(update.hintApplied)
        assertEquals(HintType.REVEAL_LETTER, update.hintType)
        assertEquals(listOf(0), update.revealedIndexes)
        assertEquals(0, update.state.hintsRemaining)
        assertEquals(1, update.state.hintsUsedTotal)
        assertTrue(update.state.hintTypesUsed.contains(HintType.REVEAL_LETTER))
    }

    @Test
    fun `eliminate hint marks wrong alphabets and consumes hint`() {
        val engine = engineFor(words = listOf("goat"), levels = 1, hintsPerLevel = 1)

        val update = engine.applyHint(HintType.ELIMINATE_LETTERS)

        assertTrue(update.hintApplied)
        assertEquals(HintType.ELIMINATE_LETTERS, update.hintType)
        assertEquals(DEFAULT_HINT_ELIMINATION_COUNT, update.eliminatedAlphabetIds.size)
        assertEquals(0, update.state.hintsRemaining)
        assertTrue(update.state.hintTypesUsed.contains(HintType.ELIMINATE_LETTERS))
    }

    @Test
    fun `hint returns error when none remain`() {
        val engine = engineFor(words = listOf("lion"), levels = 1, hintsPerLevel = 0)

        val update = engine.applyHint(HintType.REVEAL_LETTER)

        assertFalse(update.hintApplied)
        assertEquals(HintError.NO_HINTS_REMAINING, update.hintError)
    }

    private fun engineFor(
        words: List<String>,
        levels: Int,
        hintsPerLevel: Int = 1,
    ): GameSessionEngine {
        return GameSessionEngine(
            guessingWordsForCurrentGame = words.map(::Words),
            levelsPerGame = levels,
            hintsPerLevel = hintsPerLevel,
        )
    }

    private fun guessWord(engine: GameSessionEngine, word: String) {
        word
            .filter { it.isLetter() }
            .map { it.uppercaseChar() }
            .toSet()
            .forEach { letter ->
                engine.guessAlphabet(idFor(letter))
            }
    }

    private fun idFor(letter: Char): Int {
        return letter.uppercaseChar() - 'A' + 1
    }

    private fun idFor(letter: String): Int {
        return idFor(letter.single())
    }
}
