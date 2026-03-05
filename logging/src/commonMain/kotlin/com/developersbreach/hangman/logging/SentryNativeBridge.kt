package com.developersbreach.hangman.logging

import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.SentryAttributes
import io.sentry.kotlin.multiplatform.SentryLevel

internal object SentryNativeBridge {
    private val state = SentryBridgeState()

    fun configure(enabled: Boolean, dsn: String?) {
        configureSentryOnce(state, enabled, dsn) { normalizedDsn ->
            Sentry.init { options ->
                options.dsn = normalizedDsn
                options.enableAutoSessionTracking = true
                options.logs.enabled = true
            }
        }
    }

    fun recordStructuredLog(
        level: LogLevel,
        message: String,
        attributes: Map<String, String>,
    ) {
        if (!state.isInitialized) {
            return
        }
        runCatching {
            when (level) {
                LogLevel.DEBUG -> Sentry.logger.debug(message) { addAttributes(attributes) }
                LogLevel.INFO -> Sentry.logger.info(message) { addAttributes(attributes) }
                LogLevel.WARN -> Sentry.logger.warn(message) { addAttributes(attributes) }
                LogLevel.ERROR -> Sentry.logger.error(message) { addAttributes(attributes) }
                LogLevel.NONE -> Sentry.logger.info(message) { addAttributes(attributes) }
            }
        }
    }

    fun recordException(event: LogEvent) {
        if (!state.isInitialized) {
            return
        }
        val throwable = event.throwable ?: return
        runCatching {
            Sentry.captureException(throwable) { scope ->
                scope.level = event.level.toSentryLevel()
                scope.setTag("log.tag", event.tag)
                scope.setExtra("log.message", event.message)
            }
        }
    }
}

private fun SentryAttributes.addAttributes(attributes: Map<String, String>) {
    attributes.forEach { (key, value) ->
        this[key] = value
    }
}

private fun LogLevel.toSentryLevel(): SentryLevel = when (this) {
    LogLevel.DEBUG -> SentryLevel.DEBUG
    LogLevel.INFO -> SentryLevel.INFO
    LogLevel.WARN -> SentryLevel.WARNING
    LogLevel.ERROR -> SentryLevel.ERROR
    LogLevel.NONE -> SentryLevel.INFO
}
