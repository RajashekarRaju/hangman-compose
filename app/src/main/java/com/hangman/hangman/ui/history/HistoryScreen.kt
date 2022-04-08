package com.hangman.hangman.ui.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hangman.hangman.HangmanApp
import com.hangman.hangman.repository.GameRepository
import com.hangman.hangman.repository.database.entity.HistoryEntity

@Composable
fun HistoryScreen(
    navigateUp: () -> Unit,
    application: HangmanApp,
    repository: GameRepository
) {
    val viewModel = viewModel(
        factory = HistoryViewModel.provideFactory(application, repository),
        modelClass = HistoryViewModel::class.java
    )

    Surface(
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            topBar = { HistoryAppBar(navigateUp) }
        ) {
            val gameHistoryList = viewModel.history
            HistoryScreenContent(gameHistoryList)
        }
    }
}

@Composable
private fun HistoryScreenContent(
    gameHistoryList: List<HistoryEntity>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(gameHistoryList) { history ->
            ItemGameHistory(history)
        }
    }
}

@Composable
private fun ItemGameHistory(
    history: HistoryEntity
) {
    val summary = if (history.gameSummary) "Won" else "Lost"

    val difficulty = when (history.gameDifficulty) {
        1 -> "Easy"
        2 -> "Medium"
        3 -> "Hard"
        else -> "N/A"
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {

        val (
            gameScore, gameSummaryText, gameDifficultyText,
            gamePlayedTimeText, gamePlayedDateText
        ) = createRefs()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.constrainAs(gameScore) {
                start.linkTo(parent.start, 12.dp)
                centerVerticallyTo(parent)
            }
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(color = MaterialTheme.colors.background)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.onBackground.copy(0.50f),
                        shape = CircleShape
                    )
            ) {
                Text(
                    text = history.gameScore.toString(),
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Lvl ${history.gameLevel}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.primary.copy(0.75f),
                textAlign = TextAlign.Center,
            )
        }

        Text(
            text = summary,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(gameSummaryText) {
                start.linkTo(gameScore.end, 28.dp)
                top.linkTo(parent.top)
                bottom.linkTo(gameDifficultyText.top)
            }
        )

        Text(
            text = difficulty,
            style = MaterialTheme.typography.subtitle2,
            color = MaterialTheme.colors.primary,
            modifier = Modifier.constrainAs(gameDifficultyText) {
                start.linkTo(gameScore.end, 28.dp)
                top.linkTo(gameSummaryText.bottom, 1.dp)
                bottom.linkTo(parent.bottom)
            }
        )

        Text(
            text = "02:45 pm",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary.copy(0.75f),
            modifier = Modifier.constrainAs(gamePlayedTimeText) {
                end.linkTo(parent.end, 4.dp)
                top.linkTo(parent.top, 4.dp)
            }
        )

        Text(
            text = "07 apr",
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.primary.copy(0.75f),
            modifier = Modifier.constrainAs(gamePlayedDateText) {
                end.linkTo(parent.end, 4.dp)
                top.linkTo(gamePlayedTimeText.bottom, 4.dp)
            }
        )
    }
}
