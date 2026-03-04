package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import com.developersbreach.hangman.repository.CursorStyle
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
private const val CURSOR_FRAME_DELAY_MS = 60
private const val CURSOR_SIZE_PX = 28
private const val COMMON_CURSOR_RESOURCE_BASE_PATH =
    "composeResources/com.developersbreach.hangman.feature.common.ui.generated.resources/drawable/"
private const val SKULL_CURSOR_FILE_NAME = "cursor_skull.png"
private const val DEMON_CURSOR_FILE_NAME = "cursor_demon.png"
private const val HAND_CURSOR_FILE_NAME = "cursor_hand.png"
private const val HAND_BONES_CURSOR_FILE_NAME = "cursor_hand_bones.png"
private const val BONE_CURSOR_FILE_NAME = "cursor_bone.png"

private fun cursorResourcePath(fileName: String): String = COMMON_CURSOR_RESOURCE_BASE_PATH + fileName

private fun buildAnimatedCursors(resourcePath: String): List<Cursor> {
    val resourceStream = Thread.currentThread().contextClassLoader
        ?.getResourceAsStream(resourcePath)
    val source = resourceStream?.use { ImageIO.read(it) }
    if (source == null) {
        return listOf(Cursor.getDefaultCursor())
    }
    val size = CURSOR_SIZE_PX.coerceAtMost(min(source.width, source.height))
    val scaled = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB).also { image ->
        val g = image.createGraphics()
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g.drawImage(source, 0, 0, size, size, null)
        g.dispose()
    }
    return (0 until CURSOR_FRAME_COUNT).map { index ->
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
            "custom-cursor-$resourcePath-$index",
        )
    }
}

internal actual fun Modifier.platformHideNativeCursor(enabled: Boolean): Modifier = this

private val animatedCursorsByStyle: Map<CursorStyle, List<Cursor>> by lazy {
    mapOf(
        CursorStyle.SKULL to buildAnimatedCursors(cursorResourcePath(SKULL_CURSOR_FILE_NAME)),
        CursorStyle.DEMON to buildAnimatedCursors(cursorResourcePath(DEMON_CURSOR_FILE_NAME)),
        CursorStyle.HAND to buildAnimatedCursors(cursorResourcePath(HAND_CURSOR_FILE_NAME)),
        CursorStyle.HAND_BONES to buildAnimatedCursors(cursorResourcePath(HAND_BONES_CURSOR_FILE_NAME)),
        CursorStyle.BONE to buildAnimatedCursors(cursorResourcePath(BONE_CURSOR_FILE_NAME)),
    )
}

internal actual fun isCustomCursorSupported(cursorStyle: CursorStyle): Boolean {
    return animatedCursorsByStyle.containsKey(cursorStyle)
}

@Composable
internal actual fun PlatformCursorSideEffect(
    enabled: Boolean,
    cursorStyle: CursorStyle,
) {
    DisposableEffect(enabled, cursorStyle) {
        val cursors = animatedCursorsByStyle[cursorStyle]
        if (!enabled || cursors.isNullOrEmpty()) {
            Window.getWindows().forEach { window ->
                if (window.isDisplayable) {
                    window.cursor = Cursor.getDefaultCursor()
                }
            }
            onDispose {
                Window.getWindows().forEach { window ->
                    if (window.isDisplayable) {
                        window.cursor = Cursor.getDefaultCursor()
                    }
                }
            }
        } else {
            var frameIndex = 0
            val timer = Timer(CURSOR_FRAME_DELAY_MS) {
                val cursor = cursors[frameIndex]
                Window.getWindows().forEach { window ->
                    if (window.isDisplayable) {
                        window.cursor = cursor
                    }
                }
                frameIndex = (frameIndex + 1) % cursors.size
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
