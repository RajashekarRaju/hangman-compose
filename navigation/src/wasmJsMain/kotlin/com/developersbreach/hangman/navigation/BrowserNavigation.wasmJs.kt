package com.developersbreach.hangman.navigation

import kotlinx.browser.window

internal actual fun initialDestinationFromUrl(default: Destination): Destination {
    return destinationFromHash(window.location.hash) ?: default
}

internal actual fun updateUrlForDestination(destination: Destination) {
    val nextHash = hashForDestination(destination)
    if (window.location.hash != nextHash) {
        window.location.hash = nextHash
    }
}

internal actual fun setBrowserNavigationListener(
    onDestinationChanged: (Destination) -> Unit
): () -> Unit {
    val previous = window.onhashchange
    window.onhashchange = {
        destinationFromHash(window.location.hash)?.let(onDestinationChanged)
    }
    return {
        window.onhashchange = previous
    }
}

private fun destinationFromHash(hash: String): Destination? {
    val normalizedHash = normalizeHash(hash)
    return RouteSpec.fromHash(normalizedHash)?.destination
}

private fun hashForDestination(destination: Destination): String {
    return RouteSpec.fromDestination(destination)?.hash ?: RouteSpec.root.hash
}

private fun normalizeHash(hash: String): String {
    if (hash.isBlank()) return RouteSpec.root.hash
    val cleaned = hash.removePrefix("#")
    val withSlash = if (cleaned.startsWith("/")) cleaned else "/$cleaned"
    return "#$withSlash"
}
