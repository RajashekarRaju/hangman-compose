package com.developersbreach.hangman.logging

import java.io.File
import java.util.Properties

fun initializeDesktopLoggingFromRuntime() {
    val sentryDsn = readSentryDsnFromSystemProperty()
        ?: readSentryDsnFromLocalProperties()
    initializeLogging(
        LoggingInitializationConfig(
            minLevel = LogLevel.DEBUG,
            sentryDsn = sentryDsn,
            sentryEnabled = !sentryDsn.isNullOrBlank(),
        ),
    )
}

private fun readSentryDsnFromSystemProperty(): String? {
    return System.getProperty("hangman.sentry.dsn")
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
}

private fun readSentryDsnFromLocalProperties(): String? {
    val file = findLocalPropertiesFile() ?: return null
    val properties = Properties()
    file.inputStream().use(properties::load)
    return properties.getProperty("SENTRY_DSN")
        ?.trim()
        ?.takeIf { it.isNotEmpty() }
}

private fun findLocalPropertiesFile(): File? {
    var current: File? = File(System.getProperty("user.dir"))
    repeat(4) {
        val candidate = current?.resolve("local.properties")
        if (candidate?.isFile == true) {
            return candidate
        }
        current = current?.parentFile
    }
    return null
}
