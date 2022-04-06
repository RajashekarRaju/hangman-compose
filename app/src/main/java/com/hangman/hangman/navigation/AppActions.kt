package com.hangman.hangman.navigation

import androidx.navigation.NavHostController

class AppActions(
    private val navController: NavHostController,
    private val routes: AppDestinations
) {
    val navigateToGameScreen = {
        navController.navigate(routes.GAME_SCREEN_ROUTE)
    }

    val navigateToHistoryScreen = {
        navController.navigate(routes.HISTORY_SCREEN_ROUTE)
    }

    fun finishActivity(
        finishActivity: Unit
    ) = finishActivity

    // Navigates to previous screen from current screen.
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}