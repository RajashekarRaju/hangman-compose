package com.hangman.hangman.ui.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.hangman.hangman.R
import com.hangman.hangman.repository.database.entity.HistoryEntity
import com.hangman.hangman.ui.game.CreateCircularProgressIndicator
import com.hangman.hangman.ui.game.animateCurrentLevelProgress
import org.koin.androidx.compose.getViewModel


/**
 * History screen, can be navigated from onboarding screen.
 * This screen has it's own ViewModel [HistoryViewModel]
 */
@Composable
fun HistoryScreen(
    navigateUp: () -> Unit
) {
    // Create ViewModel instance with koin.
    val viewModel = getViewModel<HistoryViewModel>()
    // Get all the game history list.
    val gameHistoryList by viewModel.gameHistoryList.observeAsState(emptyList())

    Surface(
        color = MaterialTheme.colors.background
    ) {
        Scaffold(
            topBar = {
                HistoryAppBar(
                    navigateUp = navigateUp,
                    showDeleteIconInAppBar = gameHistoryList.isNotEmpty()
                ) {
                    viewModel.deleteAllGameHistoryData()
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
                    viewModel = viewModel
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
        painter = painterResource(id = R.drawable.bg_dodge),
        contentDescription = stringResource(id = R.string.cd_image_screen_bg),
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        alpha = 0.20f
    )
}

/**
 * Main content for history screen with game history list.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HistoryScreenContent(
    gameHistoryList: List<HistoryEntity>,
    viewModel: HistoryViewModel
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
                        viewModel.deleteSelectedGameHistory(history)
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

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 8.dp, vertical = 12.dp)
    ) {

        val (
            gameScore, gameSummaryText, gameDifficultyText,
            gameCategory, horDivider, gamePlayedTimeText, gamePlayedDateText
        ) = createRefs()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.constrainAs(gameScore) {
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
                    progressColor = MaterialTheme.colors.primary.copy(0.25f),
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
