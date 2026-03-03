package com.developersbreach.hangman.composeapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.developersbreach.hangman.navigation.AppNavigation
import com.developersbreach.hangman.ui.common.notification.AchievementUnlockBanner
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePalettes
import org.koin.core.context.startKoin
import org.koin.compose.viewmodel.koinViewModel

private var koinInitialized = false

fun initKoinIfNeeded() {
    if (!koinInitialized) {
        startKoin {
            modules(initKoinComponents())
        }
        koinInitialized = true
    }
}

@Composable
fun HangmanRoot(closeApplication: () -> Unit) {
    val viewModel = koinViewModel<AppInitializerViewModel>()
    val appState by viewModel.appState.collectAsState()
    val customCursorEnabled = isCustomCursorSupported(appState.cursorStyle)

    HangmanTheme(
        palette = ThemePalettes.byId(appState.themePaletteId)
    ) {
        key(appState.appLanguage) {
            ThemedSkullCursorContainer(
                enabled = customCursorEnabled,
                cursorStyle = appState.cursorStyle,
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(closeApplication = closeApplication)
                    AchievementUnlockBanner(
                        state = appState.achievementBannerState,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 12.dp),
                    )
                }
            }
        }
    }
}
