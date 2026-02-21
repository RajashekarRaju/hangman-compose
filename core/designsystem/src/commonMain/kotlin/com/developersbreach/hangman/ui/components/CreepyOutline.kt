package com.developersbreach.hangman.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.max
import kotlin.math.min
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

fun Modifier.creepyOutline(
    seed: Int,
    threshold: Float,
    fillColor: Color,
    outlineColor: Color,
    forceRectangular: Boolean = false,
    strokeWidthFactor: Float = 0.08f,
    phase: Float = 0f,
): Modifier {
    return drawWithCache {
        val randomness = threshold.coerceIn(0f, 0.45f)
        val width = size.width
        val height = size.height
        if (width <= 0f || height <= 0f) {
            onDrawBehind {}
        } else {
            val minDimension = min(width, height)
            val strokeWidth = max(1f, minDimension * strokeWidthFactor.coerceIn(0.01f, 0.20f))
            val stroke = Stroke(width = strokeWidth)
            val aspectRatio = if (height == 0f) 1f else width / height

            val path = if (!forceRectangular && aspectRatio in 0.7f..1.4f && minDimension >= 20f) {
                buildCircularCreepyPath(
                    width = width,
                    height = height,
                    randomness = randomness,
                    seed = seed,
                    phase = phase,
                )
            } else {
                buildRectangularCreepyPath(
                    width = width,
                    height = height,
                    randomness = randomness,
                    seed = seed,
                    strokeWidth = strokeWidth,
                    phase = phase,
                )
            }

            onDrawBehind {
                drawPath(path = path, color = fillColor)
                drawPath(path = path, color = outlineColor, style = stroke)
            }
        }
    }
}

private fun buildCircularCreepyPath(
    width: Float,
    height: Float,
    randomness: Float,
    seed: Int,
    phase: Float,
): Path {
    val centerX = width / 2f
    val centerY = height / 2f
    val baseRadius = (min(width, height) / 2f) * 0.82f
    val pointCount = 28
    val random = Random(seed)
    val travel = phase * 1.6f
    return Path().apply {
        for (index in 0 until pointCount) {
            val t = index.toFloat() / pointCount
            val angle = (2 * PI * t).toFloat()
            val randomOffset = (random.nextFloat() * 2f - 1f) * randomness
            val animatedOffset = if (phase == 0f || randomness == 0f) {
                0f
            } else {
                val wave1 = sin((2f * PI * (t + travel * 0.10f)).toFloat() + seed * 0.13f)
                val wave2 = sin((2f * PI * (t * 2.8f + travel * 0.06f)).toFloat() + seed * 0.05f)
                (wave1 * 0.65f + wave2 * 0.35f) * randomness * 0.20f
            }
            val radius = baseRadius * (1f + randomOffset + animatedOffset)
            val x = centerX + cos(angle) * radius
            val y = centerY + sin(angle) * radius
            if (index == 0) moveTo(x, y) else lineTo(x, y)
        }
        close()
    }
}

private fun buildRectangularCreepyPath(
    width: Float,
    height: Float,
    randomness: Float,
    seed: Int,
    strokeWidth: Float,
    phase: Float,
): Path {
    val random = Random(seed)
    val inset = strokeWidth / 2f
    val left = inset
    val top = inset
    val right = width - inset
    val bottom = height - inset

    val edgeJitter = max(0.6f, min(width, height) * randomness * 0.30f)
    val topPoints = max(3, (width / 20f).toInt())
    val sidePoints = max(3, (height / 20f).toInt())
    val perimeter = (2f * (width + height - strokeWidth * 2f)).coerceAtLeast(1f)
    val travel = phase * 0.22f

    fun jitter(): Float = (random.nextFloat() * 2f - 1f) * edgeJitter
    fun travelingWave(distanceOnPerimeter: Float): Float {
        if (phase == 0f || randomness == 0f) return 0f
        val normalized = ((distanceOnPerimeter / perimeter) + travel) % 1f
        val wave1 = sin((2f * PI * normalized).toFloat() + seed * 0.09f)
        val wave2 = sin((2f * PI * normalized * 3.2f).toFloat() + seed * 0.03f)
        return (wave1 * 0.60f + wave2 * 0.40f) * edgeJitter * 0.30f
    }

    return Path().apply {
        moveTo(left, top)

        for (i in 1..topPoints) {
            val t = i.toFloat() / topPoints
            val x = left + (right - left) * t
            val d = (right - left) * t
            val y = top + jitter() + travelingWave(d)
            lineTo(x, y)
        }

        for (i in 1..sidePoints) {
            val t = i.toFloat() / sidePoints
            val y = top + (bottom - top) * t
            val d = (right - left) + (bottom - top) * t
            val x = right + jitter() + travelingWave(d)
            lineTo(x, y)
        }

        for (i in 1..topPoints) {
            val t = i.toFloat() / topPoints
            val x = right - (right - left) * t
            val d = (right - left) + (bottom - top) + (right - left) * t
            val y = bottom + jitter() + travelingWave(d)
            lineTo(x, y)
        }

        for (i in 1..sidePoints) {
            val t = i.toFloat() / sidePoints
            val y = bottom - (bottom - top) * t
            val d = (right - left) * 2f + (bottom - top) + (bottom - top) * t
            val x = left + jitter() + travelingWave(d)
            lineTo(x, y)
        }

        close()
    }
}
