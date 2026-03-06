package com.developersbreach.hangman.ui.game

import androidx.compose.ui.geometry.Offset
import com.developersbreach.game.core.Alphabet
import com.developersbreach.hangman.ui.game.alphabets.GuessChipIdentity
import com.developersbreach.hangman.ui.game.alphabets.buildGuessChipIdentities
import com.developersbreach.hangman.ui.game.alphabets.buildLetterReturnSnapshot
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class LetterReturnOverlayTest {

    @Test
    fun `buildGuessChipIdentities skips spaces and tracks duplicate occurrences`() {
        val identities = buildGuessChipIdentities(
            playerGuesses = listOf("a", " ", "A", "b", "a"),
        )

        assertEquals(4, identities.size)
        assertEquals(GuessChipIdentity(symbol = "a", occurrence = 0), identities[0])
        assertEquals(GuessChipIdentity(symbol = "a", occurrence = 1), identities[1])
        assertEquals(GuessChipIdentity(symbol = "b", occurrence = 0), identities[2])
        assertEquals(GuessChipIdentity(symbol = "a", occurrence = 2), identities[3])
    }

    @Test
    fun `buildLetterReturnSnapshot maps correct guesses and incorrect guessed letters`() {
        val guessA0 = GuessChipIdentity(symbol = "a", occurrence = 0)
        val guessA1 = GuessChipIdentity(symbol = "a", occurrence = 1)
        val guessB0 = GuessChipIdentity(symbol = "b", occurrence = 0)

        val snapshot = buildLetterReturnSnapshot(
            playerGuesses = listOf("a", "A", " ", "b"),
            wordToGuess = "ABA",
            alphabetsList = listOf(
                Alphabet(alphabetId = 1, alphabet = "A", isAlphabetGuessed = true),
                Alphabet(alphabetId = 2, alphabet = "B", isAlphabetGuessed = true),
                Alphabet(alphabetId = 3, alphabet = "C", isAlphabetGuessed = true),
                Alphabet(alphabetId = 4, alphabet = "D", isAlphabetGuessed = false),
            ),
            chipCentersInRoot = mapOf(
                guessA0 to Offset(30f, 40f),
                guessA1 to Offset(42f, 40f),
                guessB0 to Offset(54f, 40f),
            ),
            tileCentersByAlphabetIdInRoot = mapOf(
                3 to Offset(160f, 200f),
            ),
            tileCentersBySymbolInRoot = mapOf(
                "a" to Offset(120f, 200f),
                "b" to Offset(140f, 200f),
                "c" to Offset(160f, 200f),
            ),
            overlayRootInRoot = Offset(10f, 20f),
        )

        assertEquals(3, snapshot.correctPaths.size)
        assertEquals("A", snapshot.correctPaths[0].symbol)
        assertEquals(Offset(20f, 20f), snapshot.correctPaths[0].start)
        assertEquals(Offset(110f, 180f), snapshot.correctPaths[0].end)

        assertEquals("A", snapshot.correctPaths[1].symbol)
        assertEquals(Offset(32f, 20f), snapshot.correctPaths[1].start)
        assertEquals(Offset(110f, 180f), snapshot.correctPaths[1].end)

        assertEquals("B", snapshot.correctPaths[2].symbol)
        assertEquals(Offset(44f, 20f), snapshot.correctPaths[2].start)
        assertEquals(Offset(130f, 180f), snapshot.correctPaths[2].end)

        assertEquals(1, snapshot.incorrectPaths.size)
        val incorrect = snapshot.incorrectPaths.single()
        assertEquals("C", incorrect.symbol)
        assertEquals(Offset(150f, 180f), incorrect.end)
        assertNotEquals(incorrect.start, incorrect.end)
    }

    @Test
    fun `buildLetterReturnSnapshot drops paths when measurements are unavailable`() {
        val snapshot = buildLetterReturnSnapshot(
            playerGuesses = listOf("a", "b"),
            wordToGuess = "AB",
            alphabetsList = listOf(
                Alphabet(alphabetId = 1, alphabet = "A", isAlphabetGuessed = true),
                Alphabet(alphabetId = 2, alphabet = "B", isAlphabetGuessed = true),
            ),
            chipCentersInRoot = mapOf(
                GuessChipIdentity(symbol = "a", occurrence = 0) to Offset(10f, 20f),
            ),
            tileCentersByAlphabetIdInRoot = emptyMap(),
            tileCentersBySymbolInRoot = mapOf("a" to Offset(30f, 60f)),
            overlayRootInRoot = Offset.Zero,
        )

        assertEquals(1, snapshot.correctPaths.size)
        assertTrue(snapshot.incorrectPaths.isEmpty())
    }
}
