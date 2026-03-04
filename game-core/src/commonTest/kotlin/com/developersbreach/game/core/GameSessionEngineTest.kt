package com.developersbreach.game.core

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class GameSessionEngineTest {

    @Test
    fun `init fails when levels per game is zero`() {
        val error = assertFailsWith<IllegalArgumentException> {
            GameSessionEngine(
                guessingWordsForCurrentGame = listOf(Words("wolf")),
                levelsPerGame = 0,
            )
        }

        assertTrue(error.message.orEmpty().contains("levelsPerGame"))
    }

    @Test
    fun `init fails when max attempts is zero`() {
        val error = assertFailsWith<IllegalArgumentException> {
            GameSessionEngine(
                guessingWordsForCurrentGame = listOf(Words("wolf")),
                levelsPerGame = 1,
                maxAttempts = 0,
            )
        }

        assertTrue(error.message.orEmpty().contains("maxAttempts"))
    }

    @Test
    fun `init fails when hints per level is negative`() {
        val error = assertFailsWith<IllegalArgumentException> {
            GameSessionEngine(
                guessingWordsForCurrentGame = listOf(Words("wolf")),
                levelsPerGame = 1,
                hintsPerLevel = -1,
            )
        }

        assertTrue(error.message.orEmpty().contains("hintsPerLevel"))
    }

    @Test
    fun `init fails when elimination count is not positive`() {
        val error = assertFailsWith<IllegalArgumentException> {
            GameSessionEngine(
                guessingWordsForCurrentGame = listOf(Words("wolf")),
                levelsPerGame = 1,
                hintEliminationCount = 0,
            )
        }

        assertTrue(error.message.orEmpty().contains("hintEliminationCount"))
    }

    @Test
    fun `init fails when words are fewer than levels`() {
        val error = assertFailsWith<IllegalArgumentException> {
            GameSessionEngine(
                guessingWordsForCurrentGame = listOf(Words("wolf")),
                levelsPerGame = 2,
            )
        }

        assertTrue(error.message.orEmpty().contains("Expected at least"))
    }

    @Test
    fun `initial snapshot preserves spaces and guesses start hidden`() {
        val engine = engineFor(words = listOf("sea turtle"), levels = 1)

        val snapshot = engine.snapshot()

        assertEquals("sea turtle", snapshot.currentWord)
        assertEquals(" ", snapshot.playerGuesses[3])
        assertEquals(10, snapshot.playerGuesses.size)
        assertEquals(8, snapshot.attemptsLeftToGuess)
        assertEquals(1, snapshot.maxLevelReached)
        assertEquals(1, snapshot.hintsRemaining)
        assertEquals(0, snapshot.hintsUsedTotal)
        assertTrue(snapshot.hintTypesUsed.isEmpty())
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
    fun `invalid alphabet id is a no-op`() {
        val engine = engineFor(words = listOf("bear"), levels = 1)

        val before = engine.snapshot()
        val update = engine.guessAlphabet(alphabetId = 99)
        val after = update.state

        assertFalse(update.levelCompleted)
        assertFalse(update.gameWon)
        assertFalse(update.gameLost)
        assertEquals(before.attemptsLeftToGuess, after.attemptsLeftToGuess)
        assertContentEquals(before.playerGuesses, after.playerGuesses)
    }

    @Test
    fun `guessing already guessed alphabet is a no-op`() {
        val engine = engineFor(words = listOf("bear"), levels = 1)

        engine.guessAlphabet(idFor('B'))
        val beforeSecondGuess = engine.snapshot()
        val secondUpdate = engine.guessAlphabet(idFor('B'))

        assertFalse(secondUpdate.levelCompleted)
        assertFalse(secondUpdate.gameWon)
        assertFalse(secondUpdate.gameLost)
        assertEquals(
            beforeSecondGuess.attemptsLeftToGuess,
            secondUpdate.state.attemptsLeftToGuess,
        )
        assertContentEquals(beforeSecondGuess.playerGuesses, secondUpdate.state.playerGuesses)
    }

    @Test
    fun `correct guess reveals all occurrences of same letter`() {
        val engine = engineFor(words = listOf("yemen"), levels = 1)

        val update = engine.guessAlphabet(idFor('E'))

        assertEquals("e", update.state.playerGuesses[1])
        assertEquals("e", update.state.playerGuesses[3])
        assertFalse(update.gameLost)
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
    fun `final level completion marks game won and blocks further guesses`() {
        val engine = engineFor(
            words = listOf("wolf"),
            levels = 1,
        )
        guessWord(engine, "wolf")
        val won = engine.snapshot()

        assertTrue(won.gameOverByWinning)
        val postWin = engine.guessAlphabet(idFor('A'))
        assertFalse(postWin.gameLost)
        assertFalse(postWin.levelCompleted)
        assertTrue(postWin.state.gameOverByWinning)
        assertEquals(won.attemptsLeftToGuess, postWin.state.attemptsLeftToGuess)
    }

    @Test
    fun `loss state blocks further guesses`() {
        val engine = engineFor(words = listOf("bear"), levels = 1)
        repeat(8) { index ->
            engine.guessAlphabet(idFor("ZQXJVNMP"[index]))
        }
        val lost = engine.snapshot()
        assertTrue(lost.gameOverByNoAttemptsLeft)

        val updateAfterLoss = engine.guessAlphabet(idFor('A'))
        assertFalse(updateAfterLoss.levelCompleted)
        assertFalse(updateAfterLoss.gameWon)
        assertFalse(updateAfterLoss.gameLost)
        assertEquals(0, updateAfterLoss.state.attemptsLeftToGuess)
    }

    @Test
    fun `points use playable letters and accumulate per level`() {
        val engine = engineFor(
            words = listOf("sea turtle", "goat"),
            levels = 2,
        )

        guessWord(engine, "sea turtle")
        assertEquals(9, engine.snapshot().pointsScoredOverall)

        guessWord(engine, "goat")
        val afterWin = engine.snapshot()
        assertEquals(13, afterWin.pointsScoredOverall)
        assertTrue(afterWin.gameOverByWinning)
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
    fun `reveal hint returns game finished when already won`() {
        val engine = engineFor(words = listOf("lion"), levels = 1, hintsPerLevel = 1)
        guessWord(engine, "lion")

        val update = engine.applyHint(HintType.REVEAL_LETTER)

        assertFalse(update.hintApplied)
        assertEquals(HintError.GAME_ALREADY_FINISHED, update.hintError)
    }

    @Test
    fun `reveal hint returns no unrevealed letters for all-space word`() {
        val engine = engineFor(words = listOf("   "), levels = 1, hintsPerLevel = 1)

        val update = engine.applyHint(HintType.REVEAL_LETTER)

        assertFalse(update.hintApplied)
        assertEquals(HintError.NO_UNREVEALED_LETTERS, update.hintError)
    }

    @Test
    fun `reveal hint consumed twice keeps unique hint type record`() {
        val engine = engineFor(words = listOf("goat"), levels = 1, hintsPerLevel = 2)

        engine.applyHint(HintType.REVEAL_LETTER)
        engine.applyHint(HintType.REVEAL_LETTER)
        val snapshot = engine.snapshot()

        assertEquals(0, snapshot.hintsRemaining)
        assertEquals(2, snapshot.hintsUsedTotal)
        assertEquals(setOf(HintType.REVEAL_LETTER), snapshot.hintTypesUsed)
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
    fun `eliminate hint never eliminates letters that exist in word`() {
        val engine = engineFor(
            words = listOf("goat"),
            levels = 1,
            hintsPerLevel = 1,
            hintEliminationCount = 10,
        )

        val update = engine.applyHint(HintType.ELIMINATE_LETTERS)

        val eliminatedLetters = update.eliminatedAlphabetIds.map { id -> letterFor(id) }.toSet()
        assertFalse(eliminatedLetters.contains('G'))
        assertFalse(eliminatedLetters.contains('O'))
        assertFalse(eliminatedLetters.contains('A'))
        assertFalse(eliminatedLetters.contains('T'))
    }

    @Test
    fun `eliminate hint reports no candidates when all wrong letters are already guessed`() {
        val engine = engineFor(
            words = listOf("goat"),
            levels = 1,
            hintsPerLevel = 2,
            hintEliminationCount = 22,
        )

        val first = engine.applyHint(HintType.ELIMINATE_LETTERS)
        assertTrue(first.hintApplied)

        val second = engine.applyHint(HintType.ELIMINATE_LETTERS)
        assertFalse(second.hintApplied)
        assertEquals(HintError.NO_ELIMINATION_CANDIDATES, second.hintError)
    }

    @Test
    fun `hint returns error when none remain`() {
        val engine = engineFor(words = listOf("lion"), levels = 1, hintsPerLevel = 0)

        val update = engine.applyHint(HintType.REVEAL_LETTER)

        assertFalse(update.hintApplied)
        assertEquals(HintError.NO_HINTS_REMAINING, update.hintError)
    }

    @Test
    fun `reveal hint should keep repeated letter guessable until all occurrences are revealed`() {
        val engine = engineFor(
            words = listOf("panda"),
            levels = 1,
            hintsPerLevel = 1,
        )

        engine.guessAlphabet(idFor('P'))
        val reveal = engine.applyHint(HintType.REVEAL_LETTER)
        assertTrue(reveal.hintApplied)
        assertEquals(listOf(1), reveal.revealedIndexes)

        engine.guessAlphabet(idFor('A'))
        val snapshot = engine.snapshot()

        // Expected behavior after future fix:
        // both A positions should be revealed after guessing A.
        assertEquals("a", snapshot.playerGuesses[1])
        assertEquals("a", snapshot.playerGuesses[4])
    }

    private fun engineFor(
        words: List<String>,
        levels: Int,
        hintsPerLevel: Int = 1,
        hintEliminationCount: Int = DEFAULT_HINT_ELIMINATION_COUNT,
    ): GameSessionEngine {
        return GameSessionEngine(
            guessingWordsForCurrentGame = words.map(::Words),
            levelsPerGame = levels,
            hintsPerLevel = hintsPerLevel,
            hintEliminationCount = hintEliminationCount,
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

    private fun letterFor(alphabetId: Int): Char {
        return ('A'.code + alphabetId - 1).toChar()
    }
}
