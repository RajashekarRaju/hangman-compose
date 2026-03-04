package com.developersbreach.hangman.ui.game.traditional

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

internal fun DrawScope.drawCleanStrokedPolyline(
    points: List<Offset>,
    reveal: Float,
    mainColor: Color,
    lineWidth: Float,
    style: TraditionalHangmanStyle,
    cap: StrokeCap,
) {
    val clampedReveal = reveal.coerceIn(0f, 1f)
    if (clampedReveal <= 0f || points.size < 2) return
    val path = buildTrimmedPolylinePath(points = points, reveal = clampedReveal)

    drawPath(
        path = path,
        color = style.glowColor.copy(alpha = 0.26f),
        style = Stroke(width = lineWidth * 2.00f, cap = cap, join = StrokeJoin.Round),
    )
    drawPath(
        path = path,
        color = style.outlineColor.copy(alpha = 0.88f),
        style = Stroke(width = lineWidth * 1.44f, cap = cap, join = StrokeJoin.Round),
    )
    drawPath(
        path = path,
        color = mainColor,
        style = Stroke(width = lineWidth, cap = cap, join = StrokeJoin.Round),
    )
}

internal fun DrawScope.drawCreepyStrokedPolyline(
    points: List<Offset>,
    reveal: Float,
    mainColor: Color,
    lineWidth: Float,
    phase: Float,
    style: TraditionalHangmanStyle,
    seed: Int,
    glowColor: Color = style.glowColor,
    outlineColor: Color = style.outlineColor,
    cap: StrokeCap,
) {
    val clampedReveal = reveal.coerceIn(0f, 1f)
    if (clampedReveal <= 0f || points.size < 2) return
    val path = buildCreepyPolylinePath(
        points = points,
        reveal = clampedReveal,
        phase = phase,
        seed = seed,
        amplitude = lineWidth * style.creepiness * 1.8f,
    )

    drawPath(
        path = path,
        color = glowColor.copy(alpha = 0.36f),
        style = Stroke(width = lineWidth * 2.2f, cap = cap, join = StrokeJoin.Round),
    )
    drawPath(
        path = path,
        color = outlineColor.copy(alpha = 0.90f),
        style = Stroke(width = lineWidth * 1.48f, cap = cap, join = StrokeJoin.Round),
    )
    drawPath(
        path = path,
        color = mainColor,
        style = Stroke(width = lineWidth, cap = cap, join = StrokeJoin.Round),
    )
}

internal fun DrawScope.drawCleanStrokedSegment(
    start: Offset,
    end: Offset,
    reveal: Float,
    mainColor: Color,
    lineWidth: Float,
    style: TraditionalHangmanStyle,
    cap: StrokeCap = StrokeCap.Round,
) {
    if (reveal <= 0f) return
    val revealedEnd = start.lerp(end, reveal.coerceIn(0f, 1f))

    drawLine(
        color = style.glowColor.copy(alpha = 0.26f),
        start = start,
        end = revealedEnd,
        strokeWidth = lineWidth * 2.00f,
        cap = cap,
    )
    drawLine(
        color = style.outlineColor.copy(alpha = 0.88f),
        start = start,
        end = revealedEnd,
        strokeWidth = lineWidth * 1.44f,
        cap = cap,
    )
    drawLine(
        color = mainColor,
        start = start,
        end = revealedEnd,
        strokeWidth = lineWidth,
        cap = cap,
    )
}

internal fun DrawScope.drawCleanStrokedArc(
    center: Offset,
    radius: Float,
    reveal: Float,
    mainColor: Color,
    lineWidth: Float,
    style: TraditionalHangmanStyle,
) {
    if (reveal <= 0f) return
    val path = buildCreepyArcPath(
        center = center,
        radius = radius,
        sweepAngle = 360f * reveal.coerceIn(0f, 1f),
        phase = 0f,
        seed = 0,
        amplitude = 0f,
    )
    drawPath(
        path = path,
        color = style.glowColor.copy(alpha = 0.26f),
        style = Stroke(width = lineWidth * 2.00f, cap = StrokeCap.Round),
    )
    drawPath(
        path = path,
        color = style.outlineColor.copy(alpha = 0.88f),
        style = Stroke(width = lineWidth * 1.44f, cap = StrokeCap.Round),
    )
    drawPath(
        path = path,
        color = mainColor,
        style = Stroke(width = lineWidth, cap = StrokeCap.Round),
    )
}

internal fun DrawScope.drawFillerCreepySegment(
    start: Offset,
    end: Offset,
    reveal: Float,
    mainColor: Color,
    lineWidth: Float,
    phase: Float,
    style: TraditionalHangmanStyle,
    seed: Int,
    glowColor: Color,
    outlineColor: Color,
    cap: StrokeCap,
) {
    if (reveal <= 0f) return
    val revealedEnd = start.lerp(end, reveal.coerceIn(0f, 1f))
    val creepPath = buildDistortedFillerPath(
        start = start,
        end = revealedEnd,
        phase = phase,
        seed = seed,
        amplitude = lineWidth * (style.creepiness.coerceIn(0.10f, 0.34f) * 4.8f),
    )

    drawPath(
        path = creepPath,
        color = glowColor.copy(alpha = 0.44f),
        style = Stroke(width = lineWidth * 2.24f, cap = cap),
    )
    drawPath(
        path = creepPath,
        color = outlineColor.copy(alpha = 0.94f),
        style = Stroke(width = lineWidth * 1.50f, cap = cap),
    )
    drawPath(
        path = creepPath,
        color = mainColor,
        style = Stroke(width = lineWidth, cap = cap),
    )
}

internal fun DrawScope.drawCreepyStrokedSegment(
    start: Offset,
    end: Offset,
    reveal: Float,
    mainColor: Color,
    lineWidth: Float,
    phase: Float,
    style: TraditionalHangmanStyle,
    seed: Int,
    glowColor: Color = style.glowColor,
    outlineColor: Color = style.outlineColor,
    cap: StrokeCap = StrokeCap.Butt,
) {
    if (reveal <= 0f) return
    val revealedEnd = start.lerp(end, reveal.coerceIn(0f, 1f))
    val creepPath = buildCreepySegmentPath(
        start = start,
        end = revealedEnd,
        phase = phase,
        seed = seed,
        amplitude = lineWidth * style.creepiness * 1.8f,
    )

    drawPath(
        path = creepPath,
        color = glowColor.copy(alpha = 0.36f),
        style = Stroke(width = lineWidth * 2.2f, cap = cap),
    )
    drawPath(
        path = creepPath,
        color = outlineColor.copy(alpha = 0.90f),
        style = Stroke(width = lineWidth * 1.48f, cap = cap),
    )
    drawPath(
        path = creepPath,
        color = mainColor,
        style = Stroke(width = lineWidth, cap = cap),
    )
}

internal fun DrawScope.drawCreepyStrokedArc(
    center: Offset,
    radius: Float,
    reveal: Float,
    mainColor: Color,
    lineWidth: Float,
    phase: Float,
    style: TraditionalHangmanStyle,
    seed: Int,
) {
    if (reveal <= 0f) return
    val path = buildCreepyArcPath(
        center = center,
        radius = radius,
        sweepAngle = 360f * reveal.coerceIn(0f, 1f),
        phase = phase,
        seed = seed,
        amplitude = lineWidth * style.creepiness * 1.4f,
    )
    drawPath(
        path = path,
        color = style.glowColor.copy(alpha = 0.36f),
        style = Stroke(width = lineWidth * 2.2f, cap = StrokeCap.Round),
    )
    drawPath(
        path = path,
        color = style.outlineColor.copy(alpha = 0.90f),
        style = Stroke(width = lineWidth * 1.48f, cap = StrokeCap.Round),
    )
    drawPath(
        path = path,
        color = mainColor,
        style = Stroke(width = lineWidth, cap = StrokeCap.Round),
    )
}