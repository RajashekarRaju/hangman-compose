package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import java.awt.Point
import java.awt.Toolkit
import java.awt.Window
import java.awt.image.BufferedImage
import java.awt.Cursor
import java.awt.RenderingHints
import javax.imageio.ImageIO
import javax.swing.Timer
import kotlin.math.min

private const val CURSOR_FRAME_COUNT = 36
private const val CURSOR_FRAME_DELAY_MS = 45
private const val CURSOR_SIZE_PX = 24
private const val CURSOR_RESOURCE_PATH =
    "composeResources/com.developersbreach.hangman.feature.onboarding.generated.resources/drawable/skull.png"

private val animatedSkullCursors: List<Cursor> by lazy {
    val resourceStream = Thread.currentThread().contextClassLoader
        ?.getResourceAsStream(CURSOR_RESOURCE_PATH)
    val source = resourceStream?.use { ImageIO.read(it) }
    if (source == null) {
        listOf(Cursor.getDefaultCursor())
    } else {
        val size = CURSOR_SIZE_PX.coerceAtMost(min(source.width, source.height))
        val scaled = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB).also { image ->
            val g = image.createGraphics()
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
            g.drawImage(source, 0, 0, size, size, null)
            g.dispose()
        }
        (0 until CURSOR_FRAME_COUNT).map { index ->
            val rotated = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
            val g2 = rotated.createGraphics()
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
            g2.rotate(Math.toRadians((360.0 / CURSOR_FRAME_COUNT) * index), size / 2.0, size / 2.0)
            g2.drawImage(scaled, 0, 0, null)
            g2.dispose()
            Toolkit.getDefaultToolkit().createCustomCursor(
                rotated,
                Point(size / 2, size / 2),
                "skull-cursor-$index",
            )
        }
    }
}

internal actual fun Modifier.platformHideNativeCursor(enabled: Boolean): Modifier = this

internal actual fun isCustomSkullCursorSupported(): Boolean = true

@Composable
internal actual fun PlatformCursorSideEffect(enabled: Boolean) {
    DisposableEffect(enabled) {
        if (!enabled) {
            onDispose { }
        } else {
            var frameIndex = 0
            val timer = Timer(CURSOR_FRAME_DELAY_MS) {
                val cursor = animatedSkullCursors[frameIndex]
                Window.getWindows().forEach { window ->
                    if (window.isDisplayable) {
                        window.cursor = cursor
                    }
                }
                frameIndex = (frameIndex + 1) % animatedSkullCursors.size
            }.apply {
                isRepeats = true
                start()
            }
            onDispose {
                timer.stop()
                Window.getWindows().forEach { window ->
                    if (window.isDisplayable) {
                        window.cursor = Cursor.getDefaultCursor()
                    }
                }
            }
        }
    }
}
