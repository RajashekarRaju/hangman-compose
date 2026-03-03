package com.developersbreach.hangman.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.developersbreach.hangman.ui.achievements.AchievementsScreen
import com.developersbreach.hangman.ui.achievements.AchievementsViewModel
import com.developersbreach.hangman.ui.game.GameScreen
import com.developersbreach.hangman.ui.guide.GameGuideScreen
import com.developersbreach.hangman.ui.game.GameViewModel
import com.developersbreach.hangman.ui.history.HistoryScreen
import com.developersbreach.hangman.ui.history.HistoryViewModel
import com.developersbreach.hangman.ui.onboarding.OnBoardingScreen
import com.developersbreach.hangman.ui.onboarding.OnBoardingViewModel
import com.developersbreach.hangman.ui.settings.SettingsScreen
import com.developersbreach.hangman.ui.settings.SettingsViewModel
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
                navigateToSettingsScreen = { navController.navigateToDestination(SettingsRoute) },
                navigateToHistoryScreen = { navController.navigateToDestination(HistoryRoute) },
                navigateToAchievementsScreen = { navController.navigateToDestination(AchievementsRoute) },
                navigateToGameGuideScreen = { navController.navigateToDestination(GameGuideRoute) },
                viewModel = viewModel,
                finishActivity = closeApplication,
            )
        }

        composable<SettingsRoute> {
            val viewModel = koinViewModel<SettingsViewModel>()
            SettingsScreen(
                navigateUp = {
                    if (navController.navigateUp()) {
                        updateUrlForDestination(OnBoardingRoute)
                    } else {
                        navController.navigateToDestination(OnBoardingRoute)
                    }
                },
                viewModel = viewModel,
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

        composable<GameGuideRoute> {
            GameGuideScreen(
                navigateUp = {
                    if (navController.navigateUp()) {
                        updateUrlForDestination(RouteSpec.root.destination)
                    } else {
                        navController.navigateToDestination(RouteSpec.root.destination)
                    }
                },
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

        composable<AchievementsRoute> {
            val viewModel = koinViewModel<AchievementsViewModel>()
            AchievementsScreen(
                navigateUp = {
                    if (navController.navigateUp()) {
                        updateUrlForDestination(RouteSpec.root.destination)
                    } else {
                        navController.navigateToDestination(RouteSpec.root.destination)
                    }
                },
                viewModel = viewModel,
            )
        }
    }
}