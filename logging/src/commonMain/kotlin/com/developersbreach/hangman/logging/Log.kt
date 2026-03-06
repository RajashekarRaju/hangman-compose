package com.developersbreach.hangman.logging

private const val FALLBACK_TAG = "App"
private const val MAX_TAG_LENGTH = 23
private const val AUDIT_SOURCE = "audit"

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

    fun audit(spec: AuditSpec) {
        if (!LogConfig.sentryEnabled) {
            return
        }
        val normalizedEventType = spec.eventType.trim()
        if (normalizedEventType.isEmpty()) {
            return
        }
        sentryRecordStructuredLog(
            level = LogLevel.INFO,
            message = normalizedEventType,
            attributes = sanitizeAuditAttributes(normalizedEventType, spec.parameters),
        )
    }

    @PublishedApi
    internal inline fun emit(
        level: LogLevel,
        tag: String,
        throwable: Throwable? = null,
        message: () -> String,
    ) {
        val shouldLogToSink = level.ordinal >= LogConfig.minLevel.ordinal
        val shouldCaptureException = throwable != null && LogConfig.sentryEnabled
        if (!shouldLogToSink && !shouldCaptureException) {
            return
        }
        val event = LogEvent(
            level = level,
            tag = sanitizeTag(tag),
            message = message(),
            throwable = throwable,
        )
        if (shouldLogToSink) {
            LogConfig.sink.log(event)
        }
        if (shouldCaptureException) {
            sentryRecordException(event)
        }
    }

}

@PublishedApi
internal fun sanitizeAuditAttributes(
    eventType: String,
    parameters: Map<String, String>,
): Map<String, String> {
    val sanitizedParams = parameters
        .filterKeys { key -> key.isNotBlank() }
        .mapValues { (_, value) -> value.trim() }
    return sanitizedParams + mapOf(
        "event_type" to eventType,
        "source" to AUDIT_SOURCE,
    )
}

@PublishedApi
internal fun sanitizeTag(tag: String): String {
    val normalized = tag.trim()
    if (normalized.isEmpty()) {
        return FALLBACK_TAG
    }
    return normalized.take(MAX_TAG_LENGTH)
}
