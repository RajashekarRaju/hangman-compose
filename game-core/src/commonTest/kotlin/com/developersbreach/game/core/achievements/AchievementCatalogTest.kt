package com.developersbreach.game.core.achievements

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AchievementCatalogTest {

    @Test
    fun `catalog contains definition for every achievement id`() {
        val allIds = AchievementId.entries.toSet()
        val catalogIds = AchievementCatalog.definitions.map { definition -> definition.id }.toSet()

        assertEquals(allIds, catalogIds)
        assertEquals(AchievementId.entries.size, AchievementCatalog.definitions.size)
    }

    @Test
    fun `catalog targets are always non zero`() {
        assertTrue(
            AchievementCatalog.definitions.all { definition -> definition.target > 0 },
            "All achievement targets must be greater than 0.",
        )
    }

    @Test
    fun `collection single category achievements define required category`() {
        val categoryAchievementIds = setOf(
            AchievementId.COUNTRY_SLAYER,
            AchievementId.TONGUE_TAMER,
            AchievementId.CORPORATE_CURSE,
            AchievementId.BEAST_BINDER,
        )

        categoryAchievementIds.forEach { id ->
            val definition = AchievementCatalog.definitionFor(id)
            assertEquals(AchievementTrigger.WIN_IN_CATEGORY, definition.trigger)
            assertNotNull(definition.requiredCategory)
        }
    }

    @Test
    fun `collection single difficulty achievements define required difficulty`() {
        val difficultyAchievementIds = setOf(
            AchievementId.EASY_CLEARED,
            AchievementId.MEDIUM_CLEARED,
            AchievementId.HARD_CLEARED,
            AchievementId.VERY_HARD_CLEARED,
        )

        difficultyAchievementIds.forEach { id ->
            val definition = AchievementCatalog.definitionFor(id)
            assertEquals(AchievementTrigger.WIN_IN_DIFFICULTY, definition.trigger)
            assertNotNull(definition.requiredDifficulty)
        }
    }

    @Test
    fun `catalog includes all v1 achievements`() {
        val expectedIds = setOf(
            AchievementId.FIRST_BLOOD,
            AchievementId.GRAVE_WALKER,
            AchievementId.NIGHT_SHIFT,
            AchievementId.ETERNAL_PLAYER,
            AchievementId.FIRST_WIN,
            AchievementId.WIN_COLLECTOR,
            AchievementId.CROWN_OF_ASH,
            AchievementId.UNBROKEN,
            AchievementId.RELENTLESS,
            AchievementId.IMMORTAL_STREAK,
            AchievementId.NO_CRUTCHES,
            AchievementId.MIND_READER,
            AchievementId.PERFECT_HUNT,
            AchievementId.SURGEON,
            AchievementId.SCORE_100,
            AchievementId.SCORE_250,
            AchievementId.SCORE_500,
            AchievementId.TIME_MASTER,
            AchievementId.COUNTRY_SLAYER,
            AchievementId.TONGUE_TAMER,
            AchievementId.CORPORATE_CURSE,
            AchievementId.BEAST_BINDER,
            AchievementId.WORLD_COMPLETIONIST,
            AchievementId.EASY_CLEARED,
            AchievementId.MEDIUM_CLEARED,
            AchievementId.HARD_CLEARED,
            AchievementId.VERY_HARD_CLEARED,
            AchievementId.DIFFICULTY_MASTER,
            AchievementId.IRON_BONES,
            AchievementId.MIDNIGHT_MARATHON,
            AchievementId.GRINDSTONE,
            AchievementId.BONE_MILL,
            AchievementId.NIGHT_REAPER,
            AchievementId.REVEAL_RESTRAINT,
            AchievementId.ELIMINATE_RESTRAINT,
            AchievementId.HINT_MINIMALIST,
            AchievementId.COLD_TURKEY,
            AchievementId.TIME_KEEPER,
            AchievementId.CLOCK_BREAKER,
            AchievementId.SANDGLASS_LORD,
            AchievementId.ARCHIVIST,
            AchievementId.CURSE_COLLECTOR,
        )

        assertEquals(expectedIds, AchievementCatalog.byId.keys)
    }

    @Test
    fun `initial progress mirrors catalog target`() {
        val definition = AchievementCatalog.definitionFor(AchievementId.FIRST_BLOOD)
        val progress = definition.initialProgress()

        assertEquals(AchievementId.FIRST_BLOOD, progress.achievementId)
        assertEquals(definition.target, progress.progressTarget)
        assertEquals(0, progress.progressCurrent)
        assertEquals(false, progress.isUnlocked)
        assertEquals(false, progress.isUnread)
        assertEquals(null, progress.unlockedAtEpochMillis)
    }
}
