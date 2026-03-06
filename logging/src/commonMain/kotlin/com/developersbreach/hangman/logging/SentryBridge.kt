package com.developersbreach.hangman.logging

/**
 * Platform Sentry bridge contract.
 *
 * Target behavior:
 * - Android/Desktop: send structured logs and exceptions through sentry-kotlin-multiplatform.
 * - WasmJs: send structured logs and exceptions via browser Sentry JS bridge in index.html.
 * - iOS: send structured logs and exceptions through sentry-kotlin-multiplatform.
 */
@PublishedApi
internal expect fun configureSentry(enabled: Boolean, dsn: String?)

@PublishedApi
internal expect fun sentryRecordStructuredLog(
    level: LogLevel,
    message: String,
    attributes: Map<String, String>,
)

@PublishedApi
internal expect fun sentryRecordException(event: LogEvent)