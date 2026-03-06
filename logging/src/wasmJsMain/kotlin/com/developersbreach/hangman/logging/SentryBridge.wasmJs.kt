package com.developersbreach.hangman.logging

// Wasm path: browser Sentry SDK through JS functions declared in composeApp index.html.
@JsName("hangmanSentryInit")
private external fun hangmanSentryInit(dsn: String)

@JsName("hangmanSentryLog")
private external fun hangmanSentryLog(
    level: String,
    message: String,
    attributesJson: String,
)

@JsName("hangmanSentryCaptureException")
private external fun hangmanSentryCaptureException(message: String)

@JsName("hangmanSentryCaptureMessage")
private external fun hangmanSentryCaptureMessage(message: String)

private var sentryInitialized = false
private var sentryDsn: String? = null

@PublishedApi
internal actual fun configureSentry(enabled: Boolean, dsn: String?) {
    if (!enabled) {
        return
    }
    val normalizedDsn = dsn?.trim().orEmpty()
    if (normalizedDsn.isEmpty()) {
        return
    }
    if (sentryInitialized && sentryDsn == normalizedDsn) {
        return
    }
    runCatching {
        hangmanSentryInit(normalizedDsn)
        sentryInitialized = true
        sentryDsn = normalizedDsn
    }
}

@PublishedApi
internal actual fun sentryRecordStructuredLog(
    level: LogLevel,
    message: String,
    attributes: Map<String, String>,
) {
    if (!sentryInitialized) {
        return
    }
    runCatching {
        val attributesJson = attributes.entries.joinToString(
            prefix = "{",
            postfix = "}",
            separator = ",",
        ) { (key, value) ->
            "\"${escapeJson(key)}\":\"${escapeJson(value)}\""
        }
        hangmanSentryLog(
            level = level.toSentryLevelString(),
            message = message,
            attributesJson = attributesJson,
        )
    }
}

@PublishedApi
internal actual fun sentryRecordException(event: LogEvent) {
    if (!sentryInitialized) {
        return
    }
    val throwable = event.throwable
    runCatching {
        if (throwable != null) {
            hangmanSentryCaptureException("${event.message}: ${throwable::class.simpleName}: ${throwable.message ?: "no-message"}")
        } else {
            hangmanSentryCaptureMessage("${event.level.name} ${event.tag}: ${event.message}")
        }
    }
}

private fun LogLevel.toSentryLevelString(): String = when (this) {
    LogLevel.DEBUG -> "debug"
    LogLevel.INFO -> "info"
    LogLevel.WARN -> "warn"
    LogLevel.ERROR -> "error"
    LogLevel.NONE -> "info"
}

private fun escapeJson(value: String): String = buildString {
    value.forEach { character ->
        when (character) {
            '\\' -> append("\\\\")
            '"' -> append("\\\"")
            '\n' -> append("\\n")
            '\r' -> append("\\r")
            '\t' -> append("\\t")
            else -> append(character)
        }
    }
}
