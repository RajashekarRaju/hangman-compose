package com.developersbreach.hangman.ui.game

import com.developersbreach.game.core.MAX_ATTEMPTS_PER_LEVEL
import com.developersbreach.hangman.ui.game.traditional.computeScaffoldTimerFillFractions
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameUiStateLogicTest {

    @Test
    fun `timer fill progress inverts and clamps level time progress`() {
        assertEquals(0f, timerFillProgress(1f), absoluteTolerance = 0.0001f)
        assertEquals(1f, timerFillProgress(0f), absoluteTolerance = 0.0001f)
        assertEquals(0.25f, timerFillProgress(0.75f), absoluteTolerance = 0.0001f)
        assertEquals(0f, timerFillProgress(1.7f), absoluteTolerance = 0.0001f)
        assertEquals(1f, timerFillProgress(-0.4f), absoluteTolerance = 0.0001f)
    }

    @Test
    fun `wrong guess phases used maps attempts left from 8 to 0 into 0 to 8`() {
        for (attemptsLeft in MAX_ATTEMPTS_PER_LEVEL downTo 0) {
            val expected = MAX_ATTEMPTS_PER_LEVEL - attemptsLeft
            assertEquals(expected, wrongGuessPhasesUsed(attemptsLeft))
        }
    }

    @Test
    fun `wrong guess phases used clamps out of bounds attempts`() {
        assertEquals(0, wrongGuessPhasesUsed(99))
        assertEquals(MAX_ATTEMPTS_PER_LEVEL, wrongGuessPhasesUsed(-3))
    }

    @Test
    fun `scaffold fill maps both base branches concurrently before pole beam and rope`() {
        val leftLength = 2f
        val rightLength = 6f
        val poleLength = 3f
        val beamLength = 4f
        val ropeLength = 1f
        val basePhaseEnd = rightLength / (rightLength + poleLength + beamLength + ropeLength)

        val midBase = computeScaffoldTimerFillFractions(
            fillProgress = basePhaseEnd * 0.5f,
            leftBaseLength = leftLength,
            rightBaseLength = rightLength,
            poleLength = poleLength,
            beamLength = beamLength,
            ropeLength = ropeLength,
        )
        assertEquals(0.5f, midBase.leftBaseReveal, absoluteTolerance = 0.0001f)
        assertEquals(0.5f, midBase.rightBaseReveal, absoluteTolerance = 0.0001f)
        assertEquals(0f, midBase.poleReveal, absoluteTolerance = 0.0001f)
        assertEquals(0f, midBase.beamReveal, absoluteTolerance = 0.0001f)
        assertEquals(0f, midBase.ropeReveal, absoluteTolerance = 0.0001f)

        val atPoleConvergence = computeScaffoldTimerFillFractions(
            fillProgress = basePhaseEnd,
            leftBaseLength = leftLength,
            rightBaseLength = rightLength,
            poleLength = poleLength,
            beamLength = beamLength,
            ropeLength = ropeLength,
        )
        assertEquals(1f, atPoleConvergence.leftBaseReveal, absoluteTolerance = 0.0001f)
        assertEquals(1f, atPoleConvergence.rightBaseReveal, absoluteTolerance = 0.0001f)
        assertEquals(0f, atPoleConvergence.poleReveal, absoluteTolerance = 0.0001f)
    }

    @Test
    fun `scaffold fill transitions from pole to beam to rope in order`() {
        val fractions = computeScaffoldTimerFillFractions(
            fillProgress = 0.85f,
            leftBaseLength = 3f,
            rightBaseLength = 8f,
            poleLength = 4f,
            beamLength = 4f,
            ropeLength = 2f,
        )
        assertEquals(1f, fractions.leftBaseReveal, absoluteTolerance = 0.0001f)
        assertEquals(1f, fractions.rightBaseReveal, absoluteTolerance = 0.0001f)
        assertEquals(1f, fractions.poleReveal, absoluteTolerance = 0.0001f)
        assertTrue(fractions.beamReveal > 0f && fractions.beamReveal < 1f)
        assertEquals(0f, fractions.ropeReveal, absoluteTolerance = 0.0001f)
    }

    @Test
    fun `game ui phase compatibility flags map correctly`() {
        val lossHold = GameUiState(uiPhase = GameUiPhase.LossHold)
        assertTrue(lossHold.isInteractionLocked)
        assertEquals(LevelTransitionPhase.NONE, lossHold.levelTransitionPhase)
        assertEquals(false, lossHold.showGameLostDialog)

        val successShimmer = GameUiState(
            uiPhase = GameUiPhase.LevelSuccess(step = GameUiPhase.LevelSuccess.Step.SHIMMER),
        )
        assertTrue(successShimmer.isInteractionLocked)
        assertEquals(LevelTransitionPhase.SUCCESS_SHIMMER, successShimmer.levelTransitionPhase)

        val winDialog = GameUiState(uiPhase = GameUiPhase.WinDialog)
        assertEquals(true, winDialog.showGameWonDialog)
        assertEquals(false, winDialog.isInteractionLocked)
        assertEquals(LevelTransitionPhase.NONE, winDialog.levelTransitionPhase)
    }
}
