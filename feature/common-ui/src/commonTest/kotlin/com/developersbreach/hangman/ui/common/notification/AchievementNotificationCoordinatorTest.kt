package com.developersbreach.hangman.ui.common.notification

import com.developersbreach.game.core.achievements.AchievementId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AchievementNotificationCoordinatorTest {

    @Test
    fun `shows unlocked banners in queue order`() = runTest {
        val coordinator = DefaultAchievementNotificationCoordinator(
            scope = backgroundScope,
            visibleMillis = 100L,
            exitMillis = 20L,
        )

        coordinator.enqueueUnlocked(
            listOf(
                AchievementId.FIRST_BLOOD,
                AchievementId.FIRST_WIN,
            )
        )
        runCurrent()

        assertEquals(AchievementId.FIRST_BLOOD, coordinator.bannerState.value.payload?.id)
        assertTrue(coordinator.bannerState.value.isVisible)

        advanceTimeBy(100L)
        runCurrent()
        assertEquals(AchievementId.FIRST_BLOOD, coordinator.bannerState.value.payload?.id)
        assertFalse(coordinator.bannerState.value.isVisible)

        advanceTimeBy(20L)
        runCurrent()
        assertEquals(AchievementId.FIRST_WIN, coordinator.bannerState.value.payload?.id)
        assertTrue(coordinator.bannerState.value.isVisible)

        advanceTimeBy(100L + 20L)
        runCurrent()
        assertEquals(null, coordinator.bannerState.value.payload)
        assertFalse(coordinator.bannerState.value.isVisible)
    }

    @Test
    fun `enqueue during active banner loop is retained and shown`() = runTest(StandardTestDispatcher()) {
        val coordinator = DefaultAchievementNotificationCoordinator(
            scope = backgroundScope,
            visibleMillis = 100L,
            exitMillis = 20L,
        )

        coordinator.enqueueUnlocked(listOf(AchievementId.FIRST_BLOOD))
        runCurrent()
        assertEquals(AchievementId.FIRST_BLOOD, coordinator.bannerState.value.payload?.id)

        advanceTimeBy(60L)
        coordinator.enqueueUnlocked(listOf(AchievementId.GRAVE_WALKER))
        runCurrent()

        advanceTimeBy(60L + 20L)
        runCurrent()
        assertEquals(AchievementId.GRAVE_WALKER, coordinator.bannerState.value.payload?.id)
        assertTrue(coordinator.bannerState.value.isVisible)
    }
}
