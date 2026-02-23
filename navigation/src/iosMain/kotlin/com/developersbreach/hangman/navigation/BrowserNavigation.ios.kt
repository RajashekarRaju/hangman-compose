package com.developersbreach.hangman.navigation

internal actual fun initialDestinationFromUrl(default: Destination): Destination = default

internal actual fun updateUrlForDestination(destination: Destination) = Unit

internal actual fun setBrowserNavigationListener(onDestinationChanged: (Destination) -> Unit): () -> Unit = {}
