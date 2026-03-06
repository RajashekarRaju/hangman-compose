package com.developersbreach.hangman.logging

internal class SentryBridgeState {
    var isInitialized: Boolean = false
    var dsn: String? = null
}

internal inline fun configureSentryOnce(
    state: SentryBridgeState,
    enabled: Boolean,
    dsn: String?,
    init: (normalizedDsn: String) -> Unit,
) {
    if (!enabled) {
        return
    }
    val normalizedDsn = dsn?.trim().orEmpty()
    if (normalizedDsn.isEmpty()) {
        return
    }
    if (state.isInitialized && state.dsn == normalizedDsn) {
        return
    }
    runCatching {
        init(normalizedDsn)
        state.isInitialized = true
        state.dsn = normalizedDsn
    }
}
