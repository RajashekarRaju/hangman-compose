package com.developersbreach.hangman.logging

private const val FALLBACK_TAG = "App"
private const val MAX_TAG_LENGTH = 23

object Log {

    inline fun d(tag: String, throwable: Throwable? = null, message: () -> String) {
        emit(LogLevel.DEBUG, tag, throwable, message)
    }

    inline fun i(tag: String, throwable: Throwable? = null, message: () -> String) {
        emit(LogLevel.INFO, tag, throwable, message)
    }

    inline fun w(tag: String, throwable: Throwable? = null, message: () -> String) {
        emit(LogLevel.WARN, tag, throwable, message)
    }

    inline fun e(tag: String, throwable: Throwable? = null, message: () -> String) {
        emit(LogLevel.ERROR, tag, throwable, message)
    }

    @PublishedApi
    internal inline fun emit(
        level: LogLevel,
        tag: String,
        throwable: Throwable? = null,
        message: () -> String,
    ) {
        if (level.ordinal < LogConfig.minLevel.ordinal) {
            return
        }
        LogConfig.sink.log(
            LogEvent(
                level = level,
                tag = sanitizeTag(tag),
                message = message(),
                throwable = throwable,
            )
        )
    }
}

@PublishedApi
internal fun sanitizeTag(tag: String): String {
    val normalized = tag.trim()
    if (normalized.isEmpty()) {
        return FALLBACK_TAG
    }
    return normalized.take(MAX_TAG_LENGTH)
}
