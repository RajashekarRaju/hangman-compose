package com.developersbreach.hangman.ui.game

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.game.core.instructionsList
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_dialog_instructions_subtitle
import com.developersbreach.hangman.feature.game.generated.resources.game_dialog_instructions_title
import com.developersbreach.hangman.feature.game.generated.resources.game_label_category
import com.developersbreach.hangman.feature.game.generated.resources.game_label_difficulty
import com.developersbreach.hangman.ui.components.BodyLargeText
import com.developersbreach.hangman.ui.components.BodyMediumText
import com.developersbreach.hangman.ui.components.TitleLargeText
import com.developersbreach.hangman.ui.components.TitleMediumText
import com.developersbreach.hangman.ui.theme.HangmanTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun GameInstructionsInfoDialog(
    gameDifficulty: GameDifficulty,
    gameCategory: GameCategory,
    onDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .background(HangmanTheme.colorScheme.surfaceContainer, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 28.dp),
        ) {
            TitleMediumText(
                text = stringResource(Res.string.game_dialog_instructions_title),
                color = HangmanTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                color = HangmanTheme.colorScheme.outlineVariant,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 40.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            BodyLargeText(
                text = stringResource(Res.string.game_label_difficulty, difficultyLabel(gameDifficulty)),
                color = HangmanTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            BodyLargeText(
                text = stringResource(Res.string.game_label_category, categoryLabel(gameCategory)),
                color = HangmanTheme.colorScheme.primary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            TitleLargeText(
                text = stringResource(
                    Res.string.game_dialog_instructions_subtitle,
                    categoryLabel(gameCategory),
                ),
                color = HangmanTheme.colorScheme.primary.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = HangmanTheme.colorScheme.outlineVariant,
                thickness = 1.dp,
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(480.dp)
                    .background(HangmanTheme.colorScheme.surfaceContainer, RoundedCornerShape(16.dp)),
            ) {
                items(items = instructionsList) { item ->
                    BodyMediumText(
                        text = item,
                        color = HangmanTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
