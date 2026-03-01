package com.developersbreach.game.core.achievements

enum class AchievementRarity(
    val coinReward: Int,
) {
    COMMON(5),
    RARE(10),
    EPIC(20),
    LEGENDARY(40),
}

internal fun AchievementId.rarityFor(): AchievementRarity {
    return when (this) {
        AchievementId.IMMORTAL_STREAK,
        AchievementId.SCORE_500,
        AchievementId.WORLD_COMPLETIONIST,
        AchievementId.DIFFICULTY_MASTER,
        AchievementId.NIGHT_REAPER,
        AchievementId.COLD_TURKEY,
        AchievementId.CURSE_COLLECTOR -> AchievementRarity.LEGENDARY

        AchievementId.RELENTLESS,
        AchievementId.SURGEON,
        AchievementId.SCORE_250,
        AchievementId.TIME_MASTER,
        AchievementId.MIDNIGHT_MARATHON,
        AchievementId.BONE_MILL,
        AchievementId.HINT_MINIMALIST,
        AchievementId.CLOCK_BREAKER,
        AchievementId.SANDGLASS_LORD,
        AchievementId.ARCHIVIST -> AchievementRarity.EPIC

        AchievementId.GRAVE_WALKER,
        AchievementId.NIGHT_SHIFT,
        AchievementId.ETERNAL_PLAYER,
        AchievementId.WIN_COLLECTOR,
        AchievementId.CROWN_OF_ASH,
        AchievementId.UNBROKEN,
        AchievementId.MIND_READER,
        AchievementId.PERFECT_HUNT,
        AchievementId.SCORE_100,
        AchievementId.BEAST_BINDER,
        AchievementId.VERY_HARD_CLEARED,
        AchievementId.IRON_BONES,
        AchievementId.GRINDSTONE,
        AchievementId.REVEAL_RESTRAINT,
        AchievementId.ELIMINATE_RESTRAINT,
        AchievementId.TIME_KEEPER -> AchievementRarity.RARE

        AchievementId.FIRST_BLOOD,
        AchievementId.FIRST_WIN,
        AchievementId.NO_CRUTCHES,
        AchievementId.COUNTRY_SLAYER,
        AchievementId.TONGUE_TAMER,
        AchievementId.CORPORATE_CURSE,
        AchievementId.EASY_CLEARED,
        AchievementId.MEDIUM_CLEARED,
        AchievementId.HARD_CLEARED -> AchievementRarity.COMMON
    }
}