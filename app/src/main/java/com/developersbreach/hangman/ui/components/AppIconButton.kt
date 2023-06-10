package com.developersbreach.hangman.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun AppIconButton(
    onClickIcon: () -> Unit,
    modifier: Modifier,
    icon: ImageVector,
    contentDescription: Int
) {
    IconButton(
        onClick = onClickIcon,
        modifier = modifier.background(
            color = MaterialTheme.colors.primary.copy(0.06f),
            shape = CircleShape
        )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(contentDescription),
            tint = MaterialTheme.colors.primary,
            modifier = Modifier.alpha(0.75f)
        )
    }
}