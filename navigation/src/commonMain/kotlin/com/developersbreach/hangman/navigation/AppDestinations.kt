package com.developersbreach.hangman.navigation

import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination

@Serializable
data object OnBoardingRoute : Destination

@Serializable
data object GameRoute : Destination

@Serializable
data object HistoryRoute : Destination

internal enum class RouteSpec(
    val hash: String,
    val destination: Destination,
    val clearBackStack: Boolean = false,
) {
    ONBOARDING(hash = "#/", destination = OnBoardingRoute, clearBackStack = true),
    GAME(hash = "#/game", destination = GameRoute),
    HISTORY(hash = "#/history", destination = HistoryRoute);

    companion object {
        val root: RouteSpec = ONBOARDING

        private val byHash: Map<String, RouteSpec> = entries.associateBy { it.hash }
        private val byDestination: Map<Destination, RouteSpec> = entries.associateBy { it.destination }

        fun fromHash(hash: String): RouteSpec? {
            return byHash[hash]
        }

        fun fromDestination(destination: Destination): RouteSpec? {
            return byDestination[destination]
        }
    }
}

internal fun NavHostController.navigateToDestination(destination: Destination) {
    val route = RouteSpec.fromDestination(destination) ?: return
    navigate(destination) {
        launchSingleTop = true
        if (route.clearBackStack) {
            popUpTo(RouteSpec.root.destination) { inclusive = false }
        }
    }
    updateUrlForDestination(destination)
}