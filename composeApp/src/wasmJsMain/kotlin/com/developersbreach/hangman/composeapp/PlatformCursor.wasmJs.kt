package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import com.developersbreach.hangman.repository.CursorStyle
import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLImageElement
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.events.MouseEvent

private const val CURSOR_SIZE_PX = 28
private const val COMMON_CURSOR_RESOURCE_BASE_PATH =
    "composeResources/com.developersbreach.hangman.feature.common.ui.generated.resources/drawable/"
private const val SKULL_CURSOR_FILE_NAME = "cursor_skull.png"
private const val DEMON_CURSOR_FILE_NAME = "cursor_demon.png"
private const val HAND_CURSOR_FILE_NAME = "cursor_hand.png"
private const val HAND_BONES_CURSOR_FILE_NAME = "cursor_hand_bones.png"
private const val BONE_CURSOR_FILE_NAME = "cursor_bone.png"

private fun cursorResourcePath(fileName: String): String = COMMON_CURSOR_RESOURCE_BASE_PATH + fileName

internal actual fun Modifier.platformHideNativeCursor(enabled: Boolean): Modifier = this

internal actual fun isCustomCursorSupported(cursorStyle: CursorStyle): Boolean {
    return cursorStyle != CursorStyle.DEFAULT
}

@Composable
internal actual fun PlatformCursorSideEffect(
    enabled: Boolean,
    cursorStyle: CursorStyle,
) {
    DisposableEffect(enabled, cursorStyle) {
        val body = document.body as? HTMLElement
        val styleId = "hangman-skull-cursor-style"
        val existingStyle = document.getElementById(styleId) as? HTMLStyleElement
        val styleElement =
            existingStyle ?: (document.createElement("style") as HTMLStyleElement).apply {
                id = styleId
                textContent =
                    ".hangman-skull-cursor, .hangman-skull-cursor * { cursor: none !important; }"
                document.head?.appendChild(this)
            }
        val cursorId = "hangman-skull-cursor-overlay"
        val existingCursor = document.getElementById(cursorId) as? HTMLImageElement
        val cursor = existingCursor ?: (document.createElement("img") as HTMLImageElement).apply {
            id = cursorId
            src = cursorStyle.imagePath()
            alt = ""
            style.position = "fixed"
            style.left = "0px"
            style.top = "0px"
            style.width = "${CURSOR_SIZE_PX}px"
            style.height = "${CURSOR_SIZE_PX}px"
            style.setProperty("pointer-events", "none")
            style.setProperty("user-select", "none")
            style.transformOrigin = "center"
            style.zIndex = "2147483647"
            style.display = "none"
            document.body?.appendChild(this)
        }

        var mouseX = 0.0
        var mouseY = 0.0
        var visible = false
        var angle = 0.0
        var rafId = 0

        val onMouseMove: (org.w3c.dom.events.Event) -> Unit = mouseMove@{ rawEvent ->
            val event = rawEvent as? MouseEvent ?: return@mouseMove
            mouseX = event.clientX.toDouble()
            mouseY = event.clientY.toDouble()
            visible = true
        }
        val onMouseLeave: (org.w3c.dom.events.Event) -> Unit = {
            visible = false
        }

        fun renderFrame() {
            if (visible) {
                cursor.style.display = "block"
                cursor.style.transform =
                    "translate(${mouseX - (CURSOR_SIZE_PX / 2.0)}px, ${mouseY - (CURSOR_SIZE_PX / 2.0)}px) rotate(${angle}deg)"
                angle = (angle + cursorStyle.rotationDelta()) % 360.0
            } else {
                cursor.style.display = "none"
            }
            rafId = window.requestAnimationFrame { renderFrame() }
        }

        if (enabled) {
            cursor.src = cursorStyle.imagePath()
            body?.classList?.add("hangman-skull-cursor")
            document.addEventListener("mousemove", onMouseMove)
            document.addEventListener("mouseleave", onMouseLeave)
            renderFrame()
        } else {
            body?.classList?.remove("hangman-skull-cursor")
            cursor.style.display = "none"
        }
        onDispose {
            body?.classList?.remove("hangman-skull-cursor")
            document.removeEventListener("mousemove", onMouseMove)
            document.removeEventListener("mouseleave", onMouseLeave)
            if (rafId != 0) {
                window.cancelAnimationFrame(rafId)
            }
            if (existingStyle == null) {
                styleElement.remove()
            }
            if (existingCursor == null) {
                cursor.remove()
            } else {
                existingCursor.style.display = "none"
            }
        }
    }
}

private fun CursorStyle.imagePath(): String {
    return when (this) {
        CursorStyle.DEFAULT -> ""
        CursorStyle.SKULL -> cursorResourcePath(SKULL_CURSOR_FILE_NAME)
        CursorStyle.DEMON -> cursorResourcePath(DEMON_CURSOR_FILE_NAME)
        CursorStyle.HAND -> cursorResourcePath(HAND_CURSOR_FILE_NAME)
        CursorStyle.HAND_BONES -> cursorResourcePath(HAND_BONES_CURSOR_FILE_NAME)
        CursorStyle.BONE -> cursorResourcePath(BONE_CURSOR_FILE_NAME)
    }
}

private fun CursorStyle.rotationDelta(): Double {
    return when (this) {
        CursorStyle.DEFAULT -> 0.0
        CursorStyle.SKULL -> 0.7
        CursorStyle.DEMON -> -0.6
        CursorStyle.HAND -> 0.5
        CursorStyle.HAND_BONES -> -0.5
        CursorStyle.BONE -> 0.9
    }
}
