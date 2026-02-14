package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.developersbreach.hangman.feature.onboarding.generated.resources.Res
import com.developersbreach.hangman.feature.onboarding.generated.resources.game_background
import com.developersbreach.hangman.feature.onboarding.generated.resources.onboarding_cd_image_screen_bg
import com.developersbreach.hangman.ui.theme.HangmanTheme
import com.developersbreach.hangman.ui.theme.ThemePaletteId
import com.developersbreach.hangman.ui.theme.ThemePalettes
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnBoardingScreenUI(
    uiState: OnBoardingUiState,
    onEvent: (OnBoardingEvent) -> Unit
) {
    Surface(color = HangmanTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(Res.drawable.game_background),
                contentDescription = stringResource(Res.string.onboarding_cd_image_screen_bg),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.1f
            )

            OnBoardingScreenContent(
                uiState = uiState,
                onEvent = onEvent
            )
        }
    }
}

@Preview
@Composable
private fun OnBoardingScreenUIPreview() {
    HangmanTheme(
        darkTheme = true,
        palette = ThemePalettes.byId(ThemePaletteId.ORIGINAL)
    ) {
        OnBoardingScreenUI(
            uiState = OnBoardingUiState(highScore = 1520),
            onEvent = {}
        )
    }
}