package com.hangman.hangman.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hangman.hangman.HangmanApp
import com.hangman.hangman.MainActivity
import com.hangman.hangman.ui.game.GameScreen
import com.hangman.hangman.ui.history.HistoryScreen
import com.hangman.hangman.ui.onboarding.OnBoardingScreen

@Composable
fun AppNavigation(
    startDestination: String = AppDestinations.ONBOARDING_SCREEN_ROUTE,
    routes: AppDestinations = AppDestinations,
    activity: MainActivity,
) {
    val navController = rememberNavController()
    val actions = remember(navController) {
        AppActions(navController, routes)
    }

    val application = LocalContext.current.applicationContext as Application
    val repository = (application as HangmanApp).repository

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(
            AppDestinations.ONBOARDING_SCREEN_ROUTE
        ) {
            OnBoardingScreen(
                navigateToGameScreen = actions.navigateToGameScreen,
                navigateToHistoryScreen = actions.navigateToHistoryScreen
            ) {
                actions.finishActivity(activity.finish())
            }
        }

        composable(
            AppDestinations.GAME_SCREEN_ROUTE
        ) {
            GameScreen(
                actions.navigateUp,
                application,
                repository
            )
        }

        composable(
            AppDestinations.HISTORY_SCREEN_ROUTE
        ) {
            HistoryScreen(
                actions.navigateUp,
                application,
                repository
            )
        }
    }
}