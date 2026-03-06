package com.developersbreach.hangman.ui.game.traditional

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import kotlin.collections.zipWithNext
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt

internal fun buildTrimmedPolylinePath(
    points: List<Offset>,
    reveal: Float,
): Path {
    val clampedReveal = reveal.coerceIn(0f, 1f)
    if (points.size < 2 || clampedReveal <= 0f) return Path()

    val segments = points.zipWithNext { a, b -> ScaffoldSegment(a, b) }
    val totalLength = segments.sumOf { it.length.toDouble() }.toFloat().coerceAtLeast(0.001f)
    var remaining = totalLength * clampedReveal

    return Path().apply {
        moveTo(points.first().x, points.first().y)
        segments.forEach { segment ->
            if (remaining <= 0f) return@forEach
            val revealFraction = (remaining / segment.length).coerceIn(0f, 1f)
            val end = segment.pointAt(revealFraction)
            lineTo(end.x, end.y)
            remaining -= segment.length
        }
    }
}

internal fun buildCreepyPolylinePath(
    points: List<Offset>,
    reveal: Float,
    phase: Float,
    seed: Int,
    amplitude: Float,
): Path {
    val clampedReveal = reveal.coerceIn(0f, 1f)
    if (points.size < 2 || clampedReveal <= 0f) return Path()

    val segments = points.zipWithNext { a, b -> ScaffoldSegment(a, b) }
    val totalLength = segments.sumOf { it.length.toDouble() }.toFloat().coerceAtLeast(0.001f)
    var remaining = totalLength * clampedReveal
    val travel = phase * 0.22f

    return Path().apply {
        moveTo(points.first().x, points.first().y)
        segments.forEachIndexed { segmentIndex, segment ->
            if (remaining <= 0f) return@forEachIndexed
            val revealedPortion = (remaining / segment.length).coerceIn(0f, 1f)
            if (revealedPortion <= 0f) return@forEachIndexed

            val dx = segment.end.x - segment.start.x
            val dy = segment.end.y - segment.start.y
            val length = segment.length.coerceAtLeast(1f)
            val normalX = -dy / length
            val normalY = dx / length
            val pointCount = max(6, (length / 14f).toInt())

            for (index in 1..pointCount) {
                val rawT = index.toFloat() / pointCount.toFloat()
                if (rawT > revealedPortion) break

                val normalizedT = (rawT / revealedPortion).coerceIn(0f, 1f)
                val baseX = segment.start.x + dx * rawT
                val baseY = segment.start.y + dy * rawT
                val staticNoise = sin((seed + segmentIndex * 37) * 0.173f + index * 0.719f)
                val wave1 = sin((2f * PI * (rawT + travel)).toFloat() + seed * 0.11f + segmentIndex * 0.17f)
                val wave2 = sin((2f * PI * (rawT * 2.9f + travel * 0.63f)).toFloat() + seed * 0.07f + segmentIndex * 0.13f)
                val edgeBlend = sin((normalizedT * PI).toFloat()).coerceIn(0f, 1f)
                val offset = amplitude * edgeBlend * (staticNoise * 0.58f + wave1 * 0.30f + wave2 * 0.12f)
                val x = baseX + normalX * offset
                val y = baseY + normalY * offset
                lineTo(x, y)
            }
            remaining -= segment.length
        }
    }
}

internal fun buildDistortedFillerPath(
    start: Offset,
    end: Offset,
    phase: Float,
    seed: Int,
    amplitude: Float,
): Path {
    val dx = end.x - start.x
    val dy = end.y - start.y
    val length = sqrt(dx * dx + dy * dy).coerceAtLeast(1f)
    val normalX = -dy / length
    val normalY = dx / length
    val pointCount = max(8, (length / 10f).toInt())
    val travel = phase * 0.30f

    return Path().apply {
        for (index in 0..pointCount) {
            val t = index.toFloat() / pointCount.toFloat()
            val baseX = start.x + dx * t
            val baseY = start.y + dy * t
            val staticNoise = sin(seed * 0.133f + index * 0.911f)
            val wave1 = sin((2f * PI * (t + travel)).toFloat() + seed * 0.07f)
            val wave2 = sin((2f * PI * (t * 3.4f + travel * 0.72f)).toFloat() + seed * 0.19f)
            val wave3 = sin((2f * PI * (t * 7.1f - travel * 0.37f)).toFloat() + seed * 0.31f)
            val edgeBlend = sin((t * PI).toFloat()).coerceIn(0f, 1f)
            val offset = amplitude * edgeBlend *
                    (staticNoise * 0.48f + wave1 * 0.28f + wave2 * 0.17f + wave3 * 0.07f)
            val x = baseX + normalX * offset
            val y = baseY + normalY * offset
            if (index == 0) moveTo(x, y) else lineTo(x, y)
        }
    }
}

internal fun buildCreepySegmentPath(
    start: Offset,
    end: Offset,
    phase: Float,
    seed: Int,
    amplitude: Float,
): Path {
    val dx = end.x - start.x
    val dy = end.y - start.y
    val length = sqrt(dx * dx + dy * dy).coerceAtLeast(1f)
    val normalX = -dy / length
    val normalY = dx / length
    val pointCount = max(6, (length / 14f).toInt())
    val travel = phase * 0.22f

    return Path().apply {
        for (index in 0..pointCount) {
            val t = index.toFloat() / pointCount.toFloat()
            val baseX = start.x + dx * t
            val baseY = start.y + dy * t
            val staticNoise = sin(seed * 0.173f + index * 0.719f)
            val wave1 = sin((2f * PI * (t + travel)).toFloat() + seed * 0.11f)
            val wave2 = sin((2f * PI * (t * 2.9f + travel * 0.63f)).toFloat() + seed * 0.07f)
            val edgeBlend = sin((t * PI).toFloat()).coerceIn(0f, 1f)
            val offset = amplitude * edgeBlend * (staticNoise * 0.58f + wave1 * 0.30f + wave2 * 0.12f)
            val x = baseX + normalX * offset
            val y = baseY + normalY * offset
            if (index == 0) moveTo(x, y) else lineTo(x, y)
        }
    }
}

internal fun buildCreepyArcPath(
    center: Offset,
    radius: Float,
    sweepAngle: Float,
    phase: Float,
    seed: Int,
    amplitude: Float,
): Path {
    val clampedSweep = sweepAngle.coerceIn(0f, 360f)
    val pointCount = max(16, (clampedSweep / 9f).toInt())
    val travel = phase * 0.18f

    return Path().apply {
        for (index in 0..pointCount) {
            val t = index.toFloat() / pointCount.toFloat()
            val angleDeg = -90f + clampedSweep * t
            val angleRad = (angleDeg / 180f * PI).toFloat()
            val staticNoise = sin(seed * 0.141f + index * 0.607f)
            val wave1 = sin((2f * PI * (t + travel)).toFloat() + seed * 0.09f)
            val wave2 = sin((2f * PI * (t * 3.1f + travel * 0.70f)).toFloat() + seed * 0.05f)
            val edgeBlend = sin((t * PI).toFloat()).coerceIn(0f, 1f)
            val radialOffset = amplitude * edgeBlend * (staticNoise * 0.60f + wave1 * 0.28f + wave2 * 0.12f)
            val x = center.x + cos(angleRad) * (radius + radialOffset)
            val y = center.y + sin(angleRad) * (radius + radialOffset)
            if (index == 0) moveTo(x, y) else lineTo(x, y)
        }
    }
}

internal fun resolvePolylineProgress(
    firstLength: Float,
    firstReveal: Float,
    secondLength: Float,
    secondReveal: Float,
    thirdLength: Float,
    thirdReveal: Float,
): Float {
    val first = firstLength.coerceAtLeast(0.001f)
    val second = secondLength.coerceAtLeast(0.001f)
    val third = thirdLength.coerceAtLeast(0.001f)
    val total = first + second + third
    val covered = when {
        thirdReveal > 0f -> first + second + third * thirdReveal
        secondReveal > 0f -> first + second * secondReveal
        firstReveal > 0f -> first * firstReveal
        else -> 0f
    }
    return (covered / total).coerceIn(0f, 1f)
}

internal fun Offset.lerp(other: Offset, fraction: Float): Offset {
    val t = fraction.coerceIn(0f, 1f)
    return Offset(
        x = x + (other.x - x) * t,
        y = y + (other.y - y) * t,
    )
}