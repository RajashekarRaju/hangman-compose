package com.developersbreach.hangman.logging

@PublishedApi
internal actual fun configureSentry(enabled: Boolean, dsn: String?) {
    SentryNativeBridge.configure(enabled, dsn)
}

@PublishedApi
internal actual fun sentryRecordStructuredLog(
    level: LogLevel,
    message: String,
    attributes: Map<String, String>,
) {
    SentryNativeBridge.recordStructuredLog(level, message, attributes)
}

@PublishedApi
internal actual fun sentryRecordException(event: LogEvent) {
    SentryNativeBridge.recordException(event)
}
