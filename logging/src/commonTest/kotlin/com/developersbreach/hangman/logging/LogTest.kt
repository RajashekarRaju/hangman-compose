package com.developersbreach.hangman.logging

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class LogTest {
    @AfterTest
    fun tearDown() {
        LogConfig.reset()
    }

    @Test
    fun debugLog_doesNotEvaluateMessage_whenBelowConfiguredLevel() {
        val events = mutableListOf<LogEvent>()
        LogConfig.minLevel = LogLevel.INFO
        LogConfig.sink = LogSink { event -> events += event }

        var wasMessageEvaluated = false

        Log.d("GameViewModel") {
            wasMessageEvaluated = true
            "Hidden debug log"
        }

        assertFalse(wasMessageEvaluated)
        assertTrue(events.isEmpty())
    }

    @Test
    fun errorLog_forwardsThrowableAndMessage_toSink() {
        val events = mutableListOf<LogEvent>()
        LogConfig.minLevel = LogLevel.DEBUG
        LogConfig.sink = LogSink { event -> events += event }

        val expected = IllegalStateException("Save failed")
        Log.e("HistoryRepository", expected) { "Could not persist history" }

        assertEquals(1, events.size)
        val event = events.single()
        assertEquals(LogLevel.ERROR, event.level)
        assertEquals("HistoryRepository", event.tag)
        assertEquals("Could not persist history", event.message)
        assertEquals(expected, event.throwable)
    }

    @Test
    fun emit_sanitizesBlankAndLongTags() {
        val events = mutableListOf<LogEvent>()
        LogConfig.minLevel = LogLevel.DEBUG
        LogConfig.sink = LogSink { event -> events += event }

        Log.i("     ") { "blank tag" }
        Log.w("TagThatIsWayLongerThanTwentyThreeChars") { "long tag" }

        assertEquals(2, events.size)
        assertEquals("App", events[0].tag)
        assertEquals("TagThatIsWayLongerThanT", events[1].tag)
        assertNull(events[0].throwable)
    }

    @Test
    fun noneLevel_blocksAllLogs() {
        val events = mutableListOf<LogEvent>()
        LogConfig.minLevel = LogLevel.NONE
        LogConfig.sink = LogSink { event -> events += event }

        Log.d("Tag") { "debug" }
        Log.i("Tag") { "info" }
        Log.w("Tag") { "warn" }
        Log.e("Tag") { "error" }

        assertTrue(events.isEmpty())
    }
}
