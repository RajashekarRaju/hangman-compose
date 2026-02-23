package com.developersbreach.hangman.navigation

internal expect fun initialDestinationFromUrl(default: Destination): Destination

internal expect fun updateUrlForDestination(destination: Destination)

internal expect fun setBrowserNavigationListener(onDestinationChanged: (Destination) -> Unit): () -> Unit
