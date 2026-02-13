package com.developersbreach.hangman.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DismissValue
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.developersbreach.hangman.R
import com.developersbreach.hangman.repository.database.entity.HistoryEntity
import com.developersbreach.hangman.ui.components.CreateCircularProgressIndicator
import com.developersbreach.hangman.ui.game.animateCurrentLevelProgress
import com.developersbreach.hangman.ui.theme.RedHangmanTheme
import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

@Composable
fun HistoryScreenUI(
    navigateUp: () -> Unit,
    gameHistoryList: List<HistoryEntity>,
    deleteAllGameHistoryData: () -> Unit,
    onClickDeleteSelectedGameHistory: (history:HistoryEntity) -> Unit
) {
    Surface(
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            topBar = {
                HistoryAppBar(
                    navigateUp = navigateUp,
                    showDeleteIconInAppBar = gameHistoryList.isNotEmpty()
                ) {
                    deleteAllGameHistoryData()
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Display full screen background image.
                FullScreenHistoryBackground()

                // Game history list.
                HistoryScreenContent(
                    gameHistoryList = gameHistoryList,
                    onClickDeleteSelectedGameHistory = onClickDeleteSelectedGameHistory
                )

                // Is no history of game is available show this text.
                if (gameHistoryList.isEmpty()) {
                    ShowEmptyHistoryMessage()
                }
            }
        }
    }
}

@Composable
private fun FullScreenHistoryBackground() {
    Image(
        painter = painterResource(id = R.drawable.game_background),
        contentDescription = stringResource(id = R.string.cd_image_screen_bg),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.01f
    )
}

/**
 * Main content for history screen with game history list.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HistoryScreenContent(
    gameHistoryList: List<HistoryEntity>,
    onClickDeleteSelectedGameHistory: (history: HistoryEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(
            // Show list in reverse so that player see last game history.
            items = gameHistoryList.reversed(),
            // TO improve lazy list performance, database column id is unique for each game.
            key = { it.gameId }
        ) { history ->

            // Swipe to delete the game history any game history item.
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToStart) {
                        onClickDeleteSelectedGameHistory(history)
                    }
                    it != DismissValue.DismissedToStart
                }
            )
            SwipeToDismiss(
                state = dismissState,
                background = { }
            ) {
                ItemGameHistory(history)
            }
        }
    }
}

/**
 * Each row item for game history.
 */
@Composable
private fun ItemGameHistory(
    history: HistoryEntity
) {
    // Update text value with game summary win or lost.
    val summary = if (history.gameSummary) {
        stringResource(R.string.game_won_text)
    } else {
        stringResource(R.string.game_lost_text)
    }

    Card(
        backgroundColor = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            val (
                gameScore, gameSummaryText, gameDifficultyText,
                gameCategory, horDivider, gamePlayedTimeText, gamePlayedDateText
            ) = createRefs()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .constrainAs(gameScore) {
                        start.linkTo(parent.start, 12.dp)
                        centerVerticallyTo(parent)
                    }
            ) {
                Box {
                    CreateCircularProgressIndicator(
                        currentProgress = animateCurrentLevelProgress(history.gameLevel),
                        indicatorSize = 40.dp,
                        strokeWidth = 2.dp,
                        progressColor = MaterialTheme.colors.primary.copy(0.75f)
                    )

                    CreateCircularProgressIndicator(
                        currentProgress = 1f,
                        indicatorSize = 40.dp,
                        strokeWidth = 2.dp,
                        progressColor = MaterialTheme.colors.primary.copy(0.25f)
                    )

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
                    textAlign = TextAlign.Center
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
                text = history.gameDifficulty.name,
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.constrainAs(gameDifficultyText) {
                    start.linkTo(gameScore.end, 28.dp)
                    top.linkTo(gameSummaryText.bottom, 1.dp)
                    bottom.linkTo(parent.bottom)
                }
            )

            Text(
                text = history.gameCategory.name,
                style = MaterialTheme.typography.body1,
                letterSpacing = 4.sp,
                color = MaterialTheme.colors.primary,
                modifier = Modifier.constrainAs(gameCategory) {
                    end.linkTo(parent.end, 4.dp)
                    top.linkTo(parent.top, 4.dp)
                }
            )

            Divider(
                color = MaterialTheme.colors.primary.copy(0.50f),
                thickness = 1.dp,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .width(60.dp)
                    .constrainAs(horDivider) {
                        end.linkTo(parent.end, 4.dp)
                        top.linkTo(gameCategory.bottom, 6.dp)
                    }
            )

            Text(
                text = history.gamePlayedTime,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary.copy(0.75f),
                modifier = Modifier.constrainAs(gamePlayedTimeText) {
                    end.linkTo(parent.end, 4.dp)
                    top.linkTo(horDivider.bottom, 6.dp)
                }
            )

            Text(
                text = history.gamePlayedDate,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.primary.copy(0.75f),
                modifier = Modifier.constrainAs(gamePlayedDateText) {
                    end.linkTo(parent.end, 4.dp)
                    top.linkTo(gamePlayedTimeText.bottom, 4.dp)
                }
            )
        }
    }
}

@Composable
private fun ShowEmptyHistoryMessage() {
    Text(
        text = stringResource(id = R.string.empty_history_state),
        style = MaterialTheme.typography.h4,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.onBackground.copy(0.75f),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 48.dp)
    )
}

@Preview
@Composable
private fun HistoryScreenUIPreview() {
    RedHangmanTheme {
        HistoryScreenUI(
            navigateUp = {},
            gameHistoryList = listOf(
                HistoryEntity(
                    gameId = "atomorum",
                    gameScore = 2913,
                    gameLevel = 8499,
                    gameDifficulty = GameDifficulty.HARD,
                    gameCategory = GameCategory.LANGUAGES,
                    gameSummary = false,
                    gamePlayedTime = "interesset",
                    gamePlayedDate = "decore"
                ),
            ),
            deleteAllGameHistoryData = {},
            onClickDeleteSelectedGameHistory = {}

        )
    }
}