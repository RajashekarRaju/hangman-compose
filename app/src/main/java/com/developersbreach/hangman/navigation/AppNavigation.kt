package com.developersbreach.hangman.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.developersbreach.hangman.MainActivity
import com.developersbreach.hangman.ui.game.GameScreen
import com.developersbreach.hangman.ui.game.GameViewModel
import com.developersbreach.hangman.ui.history.HistoryScreen
import com.developersbreach.hangman.ui.history.HistoryViewModel
import com.developersbreach.hangman.ui.onboarding.OnBoardingScreen
import com.developersbreach.hangman.ui.onboarding.OnBoardingViewModel
import org.koin.androidx.compose.getViewModel

/**
 * All ViewModels instances are crated from this navigation file temporarily sort the issue from
 * values not being updated in [GameScreen].
 */
@Composable
fun AppNavigation(
    startDestination: String = AppDestinations.ONBOARDING_SCREEN_ROUTE,
    routes: AppDestinations = AppDestinations,
    activity: MainActivity
) {
    val navController = rememberNavController()
    val actions = remember(navController) {
        AppActions(navController, routes)
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            AppDestinations.ONBOARDING_SCREEN_ROUTE
        ) {
            val viewModel = getViewModel<OnBoardingViewModel>()
            OnBoardingScreen(
                navigateToGameScreen = actions.navigateToGameScreen,
                navigateToHistoryScreen = actions.navigateToHistoryScreen,
                viewModel = viewModel
            ) {
                actions.finishActivity(
                    finishActivity = activity.finish()
                )
            }
        }

        composable(
            AppDestinations.GAME_SCREEN_ROUTE
        ) {
            val viewModel = getViewModel<GameViewModel>()
            GameScreen(
                navigateUp = actions.navigateUp,
                viewModel = viewModel
            )
        }

        composable(
            AppDestinations.HISTORY_SCREEN_ROUTE
        ) {
            val viewModel = getViewModel<HistoryViewModel>()
            HistoryScreen(
                navigateUp = actions.navigateUp,
                viewModel = viewModel
            )
        }
    }
}