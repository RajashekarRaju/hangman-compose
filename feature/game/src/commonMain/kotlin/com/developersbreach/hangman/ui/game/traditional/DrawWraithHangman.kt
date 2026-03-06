package com.developersbreach.hangman.ui.game.traditional

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.PI
import kotlin.math.max
import kotlin.math.sin

internal fun DrawScope.drawWraithHangman(
    humanProgress: Float,
    timerProgress: Float,
    stageRevealProgress: Float,
    phase: Float,
    style: TraditionalHangmanStyle,
) {
    val swingPhase = (phase * 2f * PI).toFloat()
    val lineWidth = size.minDimension * 0.018f * style.lineWidthMultiplier

    val baseY = size.height * 0.91f
    val baseLeft = Offset(size.width * 0.11f, baseY)
    val baseRight = Offset(size.width * 0.89f, baseY)
    val poleBottom = Offset(size.width * 0.24f, baseY)
    val poleTop = Offset(size.width * 0.24f, size.height * 0.11f)
    val beamEnd = Offset(size.width * 0.73f, size.height * 0.11f)
    val ropeEnd = Offset(size.width * 0.73f, size.height * 0.30f)

    val baseSegment = ScaffoldSegment(baseLeft, baseRight)
    val poleSegment = ScaffoldSegment(poleBottom, poleTop)
    val beamSegment = ScaffoldSegment(poleTop, beamEnd)
    val ropeSegment = ScaffoldSegment(beamEnd, ropeEnd)

    drawScaffoldIntroSegments(
        baseSegment = baseSegment,
        poleSegment = poleSegment,
        beamSegment = beamSegment,
        ropeSegment = ropeSegment,
        revealProgress = stageRevealProgress,
        lineWidth = lineWidth,
        style = style,
    )

    val leftFillBaseSegment = ScaffoldSegment(baseLeft, poleBottom)
    val rightFillBaseSegment = ScaffoldSegment(baseRight, poleBottom)

    val fillFractions = computeScaffoldTimerFillFractions(
        fillProgress = timerProgress,
        leftBaseLength = leftFillBaseSegment.length,
        rightBaseLength = rightFillBaseSegment.length,
        poleLength = poleSegment.length,
        beamLength = beamSegment.length,
        ropeLength = ropeSegment.length,
    )

    drawScaffoldTimerFill(
        leftBaseSegment = leftFillBaseSegment,
        rightBaseSegment = rightFillBaseSegment,
        poleSegment = poleSegment,
        beamSegment = beamSegment,
        ropeSegment = ropeSegment,
        fillFractions = fillFractions,
        lineWidth = lineWidth,
        phase = phase,
        style = style,
    )

    val step = 1f / 8f
    fun reveal(index: Int): Float {
        val start = index * step
        return ((humanProgress.coerceIn(0f, 1f) - start) / step).coerceIn(0f, 1f)
    }

    val headReveal = reveal(0)
    val torsoReveal = reveal(1)
    val leftArmReveal = reveal(2)
    val rightArmReveal = reveal(3)
    val leftLegReveal = reveal(4)
    val rightLegReveal = reveal(5)
    val executePhaseOne = reveal(6)
    val executePhaseTwo = reveal(7)

    val executionPose = (executePhaseOne * 0.45f + executePhaseTwo * 0.55f).coerceIn(0f, 1f)
    val hangingReveal = max(headReveal, torsoReveal)
    val ambientSwing = sin(swingPhase * 1.12f) * (style.swingAmplitude * 0.72f * hangingReveal)
    val executionSwing = sin(swingPhase * 1.7f) * (style.swingAmplitude * 1.15f * executePhaseTwo)
    val combinedSwing = ambientSwing + executionSwing
    val tightenOffset = size.height * 0.032f * executePhaseOne
    val dropOffset = size.height * 0.092f * executePhaseTwo

    val headRadius = size.minDimension * 0.084f
    val neckBaseY = ropeEnd.y + headRadius * 1.82f
    val neck = Offset(
        x = ropeEnd.x + combinedSwing,
        y = neckBaseY - tightenOffset + dropOffset,
    )
    val nooseKnot = Offset(neck.x, neck.y - headRadius * 0.04f)
    val headCenter = Offset(neck.x, neck.y - headRadius)
    val torsoEnd = Offset(
        x = neck.x + size.width * 0.02f * executePhaseTwo,
        y = neck.y + size.height * 0.24f,
    )
    val shoulder = Offset(neck.x, neck.y + size.height * 0.064f)
    val hip = Offset(neck.x + size.width * 0.02f * executePhaseTwo, neck.y + size.height * 0.22f)

    val leftArmNeutral = Offset(shoulder.x - size.width * 0.12f, shoulder.y + size.height * 0.10f)
    val rightArmNeutral = Offset(shoulder.x + size.width * 0.12f, shoulder.y + size.height * 0.10f)
    val leftArmExecuted = Offset(shoulder.x - size.width * 0.03f, shoulder.y + size.height * 0.17f)
    val rightArmExecuted = Offset(shoulder.x + size.width * 0.03f, shoulder.y + size.height * 0.17f)
    val leftArm = leftArmNeutral.lerp(leftArmExecuted, executionPose)
    val rightArm = rightArmNeutral.lerp(rightArmExecuted, executionPose)

    val leftLegNeutral = Offset(hip.x - size.width * 0.11f, hip.y + size.height * 0.14f)
    val rightLegNeutral = Offset(hip.x + size.width * 0.11f, hip.y + size.height * 0.14f)
    val leftLegExecuted = Offset(hip.x - size.width * 0.05f, hip.y + size.height * 0.19f)
    val rightLegExecuted = Offset(hip.x + size.width * 0.05f, hip.y + size.height * 0.19f)
    val leftLeg = leftLegNeutral.lerp(leftLegExecuted, executionPose)
    val rightLeg = rightLegNeutral.lerp(rightLegExecuted, executionPose)

    val ropeToNeckReveal = max(headReveal, torsoReveal)
    drawCleanStrokedSegment(
        start = ropeEnd,
        end = nooseKnot,
        reveal = ropeToNeckReveal,
        mainColor = style.scaffoldColor,
        lineWidth = lineWidth * 0.90f,
        style = style,
        cap = StrokeCap.Butt,
    )

    if (headReveal > 0f) {
        drawCircle(
            color = style.outlineColor.copy(alpha = 0.96f),
            center = headCenter,
            radius = headRadius * 0.70f * headReveal,
        )
        drawCreepyStrokedArc(
            center = headCenter,
            radius = headRadius,
            reveal = headReveal,
            mainColor = style.bodyColor,
            lineWidth = lineWidth * 0.92f,
            phase = phase + 0.50f,
            style = style,
            seed = 4101,
        )
    }

    drawCreepyStrokedSegment(
        start = neck,
        end = torsoEnd,
        reveal = torsoReveal,
        mainColor = style.bodyColor,
        lineWidth = lineWidth,
        phase = phase + 0.62f,
        style = style,
        seed = 4102,
        cap = StrokeCap.Butt,
    )

    val leftArmEnd = shoulder.lerp(leftArm, leftArmReveal)
    val rightArmEnd = shoulder.lerp(rightArm, rightArmReveal)
    when {
        leftArmReveal > 0f && rightArmReveal > 0f -> {
            drawCreepyStrokedPolyline(
                points = listOf(leftArmEnd, shoulder, rightArmEnd),
                reveal = 1f,
                mainColor = style.bodyColor,
                lineWidth = lineWidth * 0.93f,
                phase = phase + 0.23f,
                style = style,
                seed = 4103,
                cap = StrokeCap.Butt,
            )
        }

        leftArmReveal > 0f -> {
            drawCreepyStrokedSegment(
                start = shoulder,
                end = leftArmEnd,
                reveal = 1f,
                mainColor = style.bodyColor,
                lineWidth = lineWidth * 0.93f,
                phase = phase + 0.18f,
                style = style,
                seed = 4103,
                cap = StrokeCap.Butt,
            )
        }

        rightArmReveal > 0f -> {
            drawCreepyStrokedSegment(
                start = shoulder,
                end = rightArmEnd,
                reveal = 1f,
                mainColor = style.bodyColor,
                lineWidth = lineWidth * 0.93f,
                phase = phase + 0.28f,
                style = style,
                seed = 4104,
                cap = StrokeCap.Butt,
            )
        }
    }

    val leftLegEnd = hip.lerp(leftLeg, leftLegReveal)
    val rightLegEnd = hip.lerp(rightLeg, rightLegReveal)
    when {
        leftLegReveal > 0f && rightLegReveal > 0f -> {
            drawCreepyStrokedPolyline(
                points = listOf(leftLegEnd, hip, rightLegEnd),
                reveal = 1f,
                mainColor = style.bodyColor,
                lineWidth = lineWidth * 0.93f,
                phase = phase + 0.46f,
                style = style,
                seed = 4105,
                cap = StrokeCap.Butt,
            )
        }

        leftLegReveal > 0f -> {
            drawCreepyStrokedSegment(
                start = hip,
                end = leftLegEnd,
                reveal = 1f,
                mainColor = style.bodyColor,
                lineWidth = lineWidth * 0.93f,
                phase = phase + 0.40f,
                style = style,
                seed = 4105,
                cap = StrokeCap.Butt,
            )
        }

        rightLegReveal > 0f -> {
            drawCreepyStrokedSegment(
                start = hip,
                end = rightLegEnd,
                reveal = 1f,
                mainColor = style.bodyColor,
                lineWidth = lineWidth * 0.93f,
                phase = phase + 0.52f,
                style = style,
                seed = 4106,
                cap = StrokeCap.Butt,
            )
        }
    }

    if (executePhaseOne > 0f) {
        drawCreepyStrokedSegment(
            start = nooseKnot,
            end = Offset(nooseKnot.x, nooseKnot.y + headRadius * 0.36f),
            reveal = executePhaseOne,
            mainColor = style.executionColor,
            lineWidth = lineWidth * 0.44f,
            phase = phase + 0.70f,
            style = style,
            seed = 4200,
            cap = StrokeCap.Butt,
        )
        drawCreepyStrokedArc(
            center = Offset(neck.x, neck.y + headRadius * 0.42f),
            radius = headRadius * 0.66f,
            reveal = executePhaseOne,
            mainColor = style.executionColor,
            lineWidth = lineWidth * 0.58f,
            phase = phase + 0.74f,
            style = style,
            seed = 4201,
        )
    }

    if (executePhaseTwo > 0f) {
        val ghostOffset = Offset(x = combinedSwing * 0.65f, y = 0f)
        val ghostColor = style.executionColor.copy(alpha = 0.58f * executePhaseTwo)
        drawCreepyStrokedSegment(
            start = neck + ghostOffset,
            end = torsoEnd + ghostOffset,
            reveal = 1f,
            mainColor = ghostColor,
            lineWidth = lineWidth * 0.78f,
            phase = phase + 0.88f,
            style = style,
            seed = 4301,
            cap = StrokeCap.Butt,
        )
        drawCreepyStrokedPolyline(
            points = listOf(leftLeg + ghostOffset, hip + ghostOffset, rightLeg + ghostOffset),
            reveal = 1f,
            mainColor = ghostColor,
            lineWidth = lineWidth * 0.74f,
            phase = phase + 0.96f,
            style = style,
            seed = 4302,
            cap = StrokeCap.Butt,
        )
    }
}

private fun DrawScope.drawScaffoldIntroSegments(
    baseSegment: ScaffoldSegment,
    poleSegment: ScaffoldSegment,
    beamSegment: ScaffoldSegment,
    ropeSegment: ScaffoldSegment,
    revealProgress: Float,
    lineWidth: Float,
    style: TraditionalHangmanStyle,
) {
    val clampedProgress = revealProgress.coerceIn(0f, 1f)
    if (clampedProgress <= 0f) return

    val upperLength = poleSegment.length + beamSegment.length + ropeSegment.length
    val totalLength = baseSegment.length + upperLength
    val baseEnd = (baseSegment.length / totalLength).coerceIn(0f, 1f)

    val baseReveal = phaseProgress(clampedProgress, start = 0f, end = baseEnd)
    drawCleanStrokedSegment(
        start = baseSegment.start,
        end = baseSegment.end,
        reveal = baseReveal,
        mainColor = style.scaffoldColor,
        lineWidth = lineWidth,
        style = style,
        cap = StrokeCap.Butt,
    )

    val upperReveal = phaseProgress(clampedProgress, start = baseEnd, end = 1f)
    drawCleanStrokedPolyline(
        points = listOf(poleSegment.start, poleSegment.end, beamSegment.end, ropeSegment.end),
        reveal = upperReveal,
        mainColor = style.scaffoldColor,
        lineWidth = lineWidth,
        style = style,
        cap = StrokeCap.Butt,
    )
}

private fun DrawScope.drawScaffoldTimerFill(
    leftBaseSegment: ScaffoldSegment,
    rightBaseSegment: ScaffoldSegment,
    poleSegment: ScaffoldSegment,
    beamSegment: ScaffoldSegment,
    ropeSegment: ScaffoldSegment,
    fillFractions: ScaffoldTimerFillFractions,
    lineWidth: Float,
    phase: Float,
    style: TraditionalHangmanStyle,
) {
    if (
        fillFractions.leftBaseReveal <= 0f &&
        fillFractions.rightBaseReveal <= 0f &&
        fillFractions.poleReveal <= 0f &&
        fillFractions.beamReveal <= 0f &&
        fillFractions.ropeReveal <= 0f
    ) {
        return
    }

    drawFillerCreepySegment(
        start = leftBaseSegment.start,
        end = leftBaseSegment.end,
        reveal = fillFractions.leftBaseReveal,
        mainColor = style.timerFillColor,
        lineWidth = lineWidth * 0.78f,
        phase = phase + 1.02f,
        style = style,
        seed = 5101,
        glowColor = style.timerHeadColor,
        outlineColor = style.scaffoldColor.copy(alpha = 0.40f),
        cap = StrokeCap.Butt,
    )
    drawFillerCreepySegment(
        start = rightBaseSegment.start,
        end = rightBaseSegment.end,
        reveal = fillFractions.rightBaseReveal,
        mainColor = style.timerFillColor,
        lineWidth = lineWidth * 0.78f,
        phase = phase + 1.11f,
        style = style,
        seed = 5102,
        glowColor = style.timerHeadColor,
        outlineColor = style.scaffoldColor.copy(alpha = 0.40f),
        cap = StrokeCap.Butt,
    )
    val upperProgress = resolvePolylineProgress(
        firstLength = poleSegment.length,
        firstReveal = fillFractions.poleReveal,
        secondLength = beamSegment.length,
        secondReveal = fillFractions.beamReveal,
        thirdLength = ropeSegment.length,
        thirdReveal = fillFractions.ropeReveal,
    )
    drawCreepyStrokedPolyline(
        points = listOf(poleSegment.start, poleSegment.end, beamSegment.end, ropeSegment.end),
        reveal = upperProgress,
        mainColor = style.timerFillColor,
        lineWidth = lineWidth * 0.78f,
        phase = phase + 1.28f,
        style = style,
        seed = 5103,
        glowColor = style.timerHeadColor,
        outlineColor = style.scaffoldColor.copy(alpha = 0.40f),
        cap = StrokeCap.Butt,
    )
}