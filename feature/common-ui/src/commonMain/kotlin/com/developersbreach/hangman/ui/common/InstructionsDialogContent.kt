package com.developersbreach.hangman.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.developersbreach.game.core.instructionsList
import com.developersbreach.hangman.feature.common.ui.generated.resources.Res
import com.developersbreach.hangman.feature.common.ui.generated.resources.instructions_dialog_subtitle
import com.developersbreach.hangman.feature.common.ui.generated.resources.instructions_dialog_title
import com.developersbreach.hangman.feature.common.ui.generated.resources.instructions_label_category
import com.developersbreach.hangman.feature.common.ui.generated.resources.instructions_label_difficulty
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.HangmanDivider
import com.developersbreach.hangman.ui.components.HangmanDialog
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun HangmanInstructionsDialog(
    onDismissRequest: () -> Unit,
    difficultyValue: String,
    categoryValue: String,
    modifier: Modifier = Modifier,
) {
    HangmanDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(40.dp),
        seed = 801,
        threshold = 0.025f,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            TitleMediumText(
                text = stringResource(Res.string.instructions_dialog_title),
                color = HangmanTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(16.dp))

            HangmanDivider(
                color = HangmanTheme.colorScheme.outlineVariant,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 40.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            BodyLargeText(
                text = stringResource(
                    Res.string.instructions_label_difficulty,
                    difficultyValue,
                ),
                color = HangmanTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(12.dp))

            BodyLargeText(
                text = stringResource(
                    Res.string.instructions_label_category,
                    categoryValue,
                ),
                color = HangmanTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            TitleLargeText(
                text = stringResource(
                    Res.string.instructions_dialog_subtitle,
                    categoryValue,
                ),
                color = HangmanTheme.colorScheme.primary.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            HangmanDivider(
                color = HangmanTheme.colorScheme.outlineVariant,
                thickness = 1.dp,
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .background(
                        color = HangmanTheme.colorScheme.surfaceContainer,
                        shape = RoundedCornerShape(16.dp),
                    ),
            ) {
                items(items = instructionsList) { item ->
                    BodyLargeText(
                        text = item,
                        color = HangmanTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
                        letterSpacing = 2.sp,
                    )
                }
            }
        }
    }
}
