package com.developersbreach.hangman.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.developersbreach.hangman.R
import com.developersbreach.hangman.ui.theme.RedHangmanTheme
import com.developersbreach.hangman.utils.GameCategory
import com.developersbreach.hangman.utils.GameDifficulty

@Composable
fun OnBoardingScreenUI(
    navigateToGameScreen: () -> Unit,
    navigateToHistoryScreen: () -> Unit,
    finishActivity: () -> Unit,
    highScore: Int,
    releaseBackgroundMusic: () -> Unit,
    gameDifficulty: GameDifficulty,
    updatePlayerChosenCategory: (Float) -> Unit,
    gameCategory: GameCategory,
    isBackgroundMusicPlaying: Boolean,
    onClickPlayGameBackgroundMusicOnStart: () -> Unit,
    onClickReleaseBackgroundMusic: () -> Unit,
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Full screen occupying background image.
            Image(
                painter = painterResource(id = R.drawable.game_background),
                contentDescription = stringResource(R.string.cd_image_screen_bg),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.1f
            )

            // Main screen content
            OnBoardingScreenContent(
                navigateToGameScreen = navigateToGameScreen,
                finishActivity = finishActivity,
                navigateToHistoryScreen = navigateToHistoryScreen,
                highScore = highScore,
                releaseBackgroundMusic = releaseBackgroundMusic,
                gameDifficulty = gameDifficulty,
                updatePlayerChosenCategory = updatePlayerChosenCategory,
                gameCategory = gameCategory,
                isBackgroundMusicPlaying = isBackgroundMusicPlaying,
                onClickPlayGameBackgroundMusicOnStart = onClickPlayGameBackgroundMusicOnStart,
                onClickReleaseBackgroundMusic = onClickReleaseBackgroundMusic
            )
        }
    }
}

@Preview
@Composable
private fun OnBoardingScreenUIPreview() {
    RedHangmanTheme {
        OnBoardingScreenUI(
            navigateToGameScreen = {},
            navigateToHistoryScreen = {},
            finishActivity = {},
            highScore = 1520,
            releaseBackgroundMusic = {},
            gameDifficulty = GameDifficulty.HARD,
            updatePlayerChosenCategory = {},
            gameCategory = GameCategory.LANGUAGES,
            isBackgroundMusicPlaying = false,
            onClickPlayGameBackgroundMusicOnStart = {},
            onClickReleaseBackgroundMusic = {}
        )
    }
}