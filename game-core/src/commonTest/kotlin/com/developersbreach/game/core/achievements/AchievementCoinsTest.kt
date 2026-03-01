package com.developersbreach.game.core.achievements

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AchievementCoinsTest {

    @Test
    fun `coin total sums unlocked achievement rewards`() {
        val progress = listOf(
            AchievementCatalog.definitionFor(AchievementId.FIRST_BLOOD).initialProgress().copy(
                isUnlocked = true,
                progressCurrent = 1,
            ),
            AchievementCatalog.definitionFor(AchievementId.GRAVE_WALKER).initialProgress().copy(
                isUnlocked = true,
                progressCurrent = 10,
            ),
            AchievementCatalog.definitionFor(AchievementId.NIGHT_SHIFT).initialProgress(),
        )

        assertEquals(15, totalAchievementCoins(progress))
    }

    @Test
    fun `all definitions provide positive coin rewards`() {
        assertTrue(
            AchievementCatalog.definitions.all { definition -> definition.coinReward() > 0 },
        )
    }
}
