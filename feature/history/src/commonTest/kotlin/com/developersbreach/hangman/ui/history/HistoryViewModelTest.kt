package com.developersbreach.hangman.ui.history

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty
import com.developersbreach.hangman.repository.HistoryRepository
import com.developersbreach.hangman.repository.model.HistoryRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HistoryViewModelTest {

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
    fun `history stream is mapped in reverse order`() = runTest(dispatcher) {
        val repository = FakeHistoryRepository()
        val viewModel = HistoryViewModel(repository)

        repository.emitHistory(
            historyRecord(id = "1", score = 11),
            historyRecord(id = "2", score = 22),
            historyRecord(id = "3", score = 33),
        )
        advanceUntilIdle()

        val ids = viewModel.uiState.value.gameHistoryList.map { it.history.gameId }
        assertEquals(listOf("3", "2", "1"), ids)
        assertTrue(viewModel.uiState.value.showDeleteIconInAppBar)
    }

    @Test
    fun `navigate up event emits effect`() = runTest(dispatcher) {
        val viewModel = HistoryViewModel(FakeHistoryRepository())
        advanceUntilIdle()

        val effectDeferred = async { viewModel.effects.first() }
        viewModel.onEvent(HistoryEvent.NavigateUpClicked)
        runCurrent()

        assertEquals(HistoryEffect.NavigateUp, effectDeferred.await())
    }

    @Test
    fun `delete events invoke repository`() = runTest(dispatcher) {
        val repository = FakeHistoryRepository()
        val viewModel = HistoryViewModel(repository)
        advanceUntilIdle()

        val item = historyRecord(id = "item", score = 50)
        viewModel.onEvent(HistoryEvent.DeleteHistoryItemClicked(item))
        viewModel.onEvent(HistoryEvent.DeleteAllClicked)
        advanceUntilIdle()

        assertEquals(item, repository.deletedItem)
        assertEquals(1, repository.deleteAllCalls)
    }

    @Test
    fun `empty history hides delete icon`() = runTest(dispatcher) {
        val repository = FakeHistoryRepository()
        val viewModel = HistoryViewModel(repository)
        repository.emitHistory()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.gameHistoryList.isEmpty())
        assertFalse(viewModel.uiState.value.showDeleteIconInAppBar)
    }
}

private class FakeHistoryRepository : HistoryRepository {

    private val state = MutableStateFlow<List<HistoryRecord>>(emptyList())
    var deletedItem: HistoryRecord? = null
    var deleteAllCalls: Int = 0

    override fun observeHistory() = state

    override suspend fun deleteHistoryItem(history: HistoryRecord) {
        deletedItem = history
    }

    override suspend fun deleteAllHistory() {
        deleteAllCalls += 1
    }

    fun emitHistory(vararg records: HistoryRecord) {
        state.update { records.toList() }
    }
}

private fun historyRecord(id: String, score: Int): HistoryRecord {
    return HistoryRecord(
        gameId = id,
        gameScore = score,
        gameLevel = 2,
        gameDifficulty = GameDifficulty.MEDIUM,
        gameCategory = GameCategory.ANIMALS,
        gameSummary = score > 0,
        gamePlayedTime = "09:15 AM",
        gamePlayedDate = "2026-02-24",
    )
}
