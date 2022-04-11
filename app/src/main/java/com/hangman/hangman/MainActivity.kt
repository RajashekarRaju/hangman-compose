package com.hangman.hangman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hangman.hangman.navigation.AppNavigation
import com.hangman.hangman.ui.theme.RedHangmanTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            // Let game run in immersive fullscreen mode.
            val systemUiController = rememberSystemUiController()
            systemUiController.isSystemBarsVisible = false

            RedHangmanTheme {
                AppNavigation(activity = this)
            }
        }
    }
}