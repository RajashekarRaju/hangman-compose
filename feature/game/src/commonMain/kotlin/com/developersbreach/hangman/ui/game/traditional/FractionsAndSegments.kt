package com.developersbreach.hangman.ui.game.traditional

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.sqrt

data class ScaffoldSegment(
    val start: Offset,
    val end: Offset,
) {
    val length: Float
        get() {
            val dx = end.x - start.x
            val dy = end.y - start.y
            return sqrt(dx * dx + dy * dy).coerceAtLeast(0.001f)
        }

    fun pointAt(fraction: Float): Offset = start.lerp(end, fraction)
}

internal data class TraditionalHangmanStyle(
    val scaffoldColor: Color,
    val bodyColor: Color,
    val outlineColor: Color,
    val glowColor: Color,
    val executionColor: Color,
    val timerFillColor: Color,
    val timerHeadColor: Color,
    val lineWidthMultiplier: Float,
    val swingAmplitude: Float,
    val jitterAmplitude: Float,
    val creepiness: Float,
)

internal data class ScaffoldTimerFillFractions(
    val leftBaseReveal: Float,
    val rightBaseReveal: Float,
    val poleReveal: Float,
    val beamReveal: Float,
    val ropeReveal: Float,
)

internal fun computeScaffoldTimerFillFractions(
    fillProgress: Float,
    leftBaseLength: Float,
    rightBaseLength: Float,
    poleLength: Float,
    beamLength: Float,
    ropeLength: Float,
): ScaffoldTimerFillFractions {
    val clampedProgress = fillProgress.coerceIn(0f, 1f)
    if (clampedProgress <= 0f) {
        return ScaffoldTimerFillFractions(
            leftBaseReveal = 0f,
            rightBaseReveal = 0f,
            poleReveal = 0f,
            beamReveal = 0f,
            ropeReveal = 0f,
        )
    }

    val normalizedLeft = leftBaseLength.coerceAtLeast(0.001f)
    val normalizedRight = rightBaseLength.coerceAtLeast(0.001f)
    val normalizedPole = poleLength.coerceAtLeast(0.001f)
    val normalizedBeam = beamLength.coerceAtLeast(0.001f)
    val normalizedRope = ropeLength.coerceAtLeast(0.001f)

    val baseConcurrentCost = max(normalizedLeft, normalizedRight)
    val totalCost = baseConcurrentCost + normalizedPole + normalizedBeam + normalizedRope
    val basePhaseEnd = baseConcurrentCost / totalCost
    val polePhaseEnd = basePhaseEnd + (normalizedPole / totalCost)
    val beamPhaseEnd = polePhaseEnd + (normalizedBeam / totalCost)

    return ScaffoldTimerFillFractions(
        leftBaseReveal = phaseProgress(clampedProgress, start = 0f, end = basePhaseEnd),
        rightBaseReveal = phaseProgress(clampedProgress, start = 0f, end = basePhaseEnd),
        poleReveal = phaseProgress(clampedProgress, start = basePhaseEnd, end = polePhaseEnd),
        beamReveal = phaseProgress(clampedProgress, start = polePhaseEnd, end = beamPhaseEnd),
        ropeReveal = phaseProgress(clampedProgress, start = beamPhaseEnd, end = 1f),
    )
}

internal fun phaseProgress(
    progress: Float,
    start: Float,
    end: Float,
): Float {
    val span = (end - start).coerceAtLeast(0.0001f)
    return ((progress - start) / span).coerceIn(0f, 1f)
}