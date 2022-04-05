package com.hangman.hangman.navigation

import androidx.navigation.NavHostController
import com.hangman.hangman.MainActivity

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
        activity: MainActivity
    ): () -> Unit = {
        activity.finish()
    }

    // Navigates to previous screen from current screen.
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}