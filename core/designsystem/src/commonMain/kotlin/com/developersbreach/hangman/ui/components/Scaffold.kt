package com.developersbreach.hangman.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.PaddingValues
import com.developersbreach.hangman.ui.theme.HangmanTheme

@Composable
fun HangmanScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable ColumnScope.() -> Unit = {},
    containerColor: Color = HangmanTheme.colorScheme.background,
    contentColor: Color = HangmanTheme.colorScheme.onBackground,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = { Column { bottomBar() } },
        containerColor = containerColor,
        contentColor = contentColor,
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HangmanTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = HangmanTheme.colorScheme.background,
            titleContentColor = HangmanTheme.colorScheme.onBackground,
            navigationIconContentColor = HangmanTheme.colorScheme.primary,
            actionIconContentColor = HangmanTheme.colorScheme.primary,
        ),
        title = title,
        navigationIcon = navigationIcon,
        actions = actions,
    )
}
