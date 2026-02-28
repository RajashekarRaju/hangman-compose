package com.developersbreach.hangman.ui.achievements

import com.developersbreach.game.core.achievements.AchievementCatalog
import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.game.core.achievements.AchievementProgress
import com.developersbreach.game.core.achievements.AchievementStatCounters
import com.developersbreach.game.core.achievements.initialProgress
import com.developersbreach.hangman.repository.AchievementsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AchievementsViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `summary includes unlocked locked total and coin totals`() = runTest(dispatcher) {
        val repository = FakeAchievementsRepository(
            initialProgress = listOf(
                progressFor(AchievementId.FIRST_BLOOD, unlocked = true),
                progressFor(AchievementId.GRAVE_WALKER, unlocked = true),
                progressFor(AchievementId.NIGHT_SHIFT, unlocked = false),
            )
        )

        val viewModel = AchievementsViewModel(repository)
        advanceUntilIdle()

        val summary = viewModel.uiState.value.summary
        assertEquals(2, summary.unlockedCount)
        assertEquals(AchievementCatalog.definitions.size - 2, summary.lockedCount)
        assertEquals(AchievementCatalog.definitions.size, summary.totalCount)
        assertEquals(15, summary.totalCoins)
    }
}

private class FakeAchievementsRepository(
    initialProgress: List<AchievementProgress>,
) : AchievementsRepository {
    private val progressState = MutableStateFlow(initialProgress)
    private val countersState = MutableStateFlow(AchievementStatCounters())

    override fun observeAchievementProgress() = progressState

    override suspend fun replaceAchievementProgress(progress: List<AchievementProgress>) {
        progressState.value = progress
    }

    override fun observeAchievementStatCounters() = countersState

    override suspend fun saveAchievementStatCounters(counters: AchievementStatCounters) {
        countersState.value = counters
    }
}

private fun progressFor(
    id: AchievementId,
    unlocked: Boolean,
): AchievementProgress {
    val definition = AchievementCatalog.definitionFor(id)
    return definition.initialProgress().copy(
        isUnlocked = unlocked,
        progressCurrent = if (unlocked) definition.target else 0,
    )
}
