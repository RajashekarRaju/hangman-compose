package com.developersbreach.hangman.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.developersbreach.hangman.ui.game.GameScreen
import com.developersbreach.hangman.ui.game.GameViewModel
import com.developersbreach.hangman.ui.history.HistoryScreen
import com.developersbreach.hangman.ui.history.HistoryViewModel
import com.developersbreach.hangman.ui.onboarding.OnBoardingScreen
import com.developersbreach.hangman.ui.onboarding.OnBoardingViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavigation(
    startDestination: Destination = OnBoardingRoute,
    closeApplication: () -> Unit,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<OnBoardingRoute> {
            val viewModel = koinViewModel<OnBoardingViewModel>()
            OnBoardingScreen(
                navigateToGameScreen = { navController.navigate(GameRoute) },
                navigateToHistoryScreen = { navController.navigate(HistoryRoute) },
                viewModel = viewModel,
                finishActivity = closeApplication,
            )
        }

        composable<GameRoute> {
            val viewModel = koinViewModel<GameViewModel>()
            GameScreen(
                navigateUp = { navController.navigateUp() },
                viewModel = viewModel
            )
        }

        composable<HistoryRoute> {
            val viewModel = koinViewModel<HistoryViewModel>()
            HistoryScreen(
                navigateUp = { navController.navigateUp() },
                viewModel = viewModel
            )
        }
    }
}