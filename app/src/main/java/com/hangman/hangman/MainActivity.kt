package com.hangman.hangman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.hangman.hangman.navigation.AppNavigation
import com.hangman.hangman.ui.theme.RedHangmanTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RedHangmanTheme {
                AppNavigation(activity = this)
            }
        }
    }
}