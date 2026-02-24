package com.developersbreach.hangman.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
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
    val resolvedStartDestination = remember(startDestination) {
        initialDestinationFromUrl(startDestination)
    }

    DisposableEffect(navController) {
        val clearListener = setBrowserNavigationListener { destination ->
            navController.navigateToDestination(destination)
        }
        onDispose { clearListener() }
    }

    NavHost(
        navController = navController,
        startDestination = resolvedStartDestination
    ) {
        composable<OnBoardingRoute> {
            val viewModel = koinViewModel<OnBoardingViewModel>()
            OnBoardingScreen(
                navigateToGameScreen = { navController.navigateToDestination(GameRoute) },
                navigateToHistoryScreen = { navController.navigateToDestination(HistoryRoute) },
                viewModel = viewModel,
                finishActivity = closeApplication,
            )
        }

        composable<GameRoute> {
            val viewModel = koinViewModel<GameViewModel>()
            GameScreen(
                navigateUp = {
                    if (navController.navigateUp()) {
                        updateUrlForDestination(RouteSpec.root.destination)
                    } else {
                        navController.navigateToDestination(RouteSpec.root.destination)
                    }
                },
                viewModel = viewModel
            )
        }

        composable<HistoryRoute> {
            val viewModel = koinViewModel<HistoryViewModel>()
            HistoryScreen(
                navigateUp = {
                    if (navController.navigateUp()) {
                        updateUrlForDestination(RouteSpec.root.destination)
                    } else {
                        navController.navigateToDestination(RouteSpec.root.destination)
                    }
                },
                viewModel = viewModel
            )
        }
    }
}