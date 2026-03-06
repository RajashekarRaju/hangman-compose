package com.developersbreach.game.core.achievements

import com.developersbreach.game.core.GameCategory
import com.developersbreach.game.core.GameDifficulty

object AchievementCatalog {

    val definitions: List<AchievementDefinition> = buildCatalog()

    val byId: Map<AchievementId, AchievementDefinition> = definitions.associateBy { it.id }

    init {
        require(definitions.size == AchievementId.entries.size) {
            "Achievement catalog size (${definitions.size}) must match id enum size (${AchievementId.entries.size})."
        }
        require(byId.size == definitions.size) {
            "Achievement catalog contains duplicate IDs."
        }
    }

    fun definitionFor(id: AchievementId): AchievementDefinition {
        return byId.getValue(id)
    }

    private fun buildCatalog(): List<AchievementDefinition> {
        return listOf(
            achievement(
                id = AchievementId.FIRST_BLOOD,
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_PLAYED_TOTAL,
                target = 1,
            ),
            achievement(
                id = AchievementId.GRAVE_WALKER,
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_PLAYED_TOTAL,
                target = 10,
            ),
            achievement(
                id = AchievementId.NIGHT_SHIFT,
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_PLAYED_TOTAL,
                target = 25,
            ),
            achievement(
                id = AchievementId.ETERNAL_PLAYER,
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_PLAYED_TOTAL,
                target = 50,
            ),
            achievement(
                id = AchievementId.FIRST_WIN,
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_WON_TOTAL,
                target = 1,
            ),
            achievement(
                id = AchievementId.WIN_COLLECTOR,
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_WON_TOTAL,
                target = 10,
            ),
            achievement(
                id = AchievementId.CROWN_OF_ASH,
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_WON_TOTAL,
                target = 25,
            ),
            achievement(
                id = AchievementId.UNBROKEN,
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.BEST_WIN_STREAK,
                target = 3,
            ),
            achievement(
                id = AchievementId.RELENTLESS,
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.BEST_WIN_STREAK,
                target = 5,
            ),
            achievement(
                id = AchievementId.IMMORTAL_STREAK,
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.BEST_WIN_STREAK,
                target = 10,
            ),
            achievement(
                id = AchievementId.NO_CRUTCHES,
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.WIN_WITHOUT_HINTS_TOTAL,
                target = 1,
            ),
            achievement(
                id = AchievementId.MIND_READER,
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.WIN_WITHOUT_HINTS_TOTAL,
                target = 5,
            ),
            achievement(
                id = AchievementId.PERFECT_HUNT,
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.PERFECT_WINS_TOTAL,
                target = 1,
            ),
            achievement(
                id = AchievementId.SURGEON,
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.PERFECT_WINS_TOTAL,
                target = 5,
            ),
            achievement(
                id = AchievementId.SCORE_100,
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.HIGHEST_SCORE,
                target = 100,
            ),
            achievement(
                id = AchievementId.SCORE_250,
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.HIGHEST_SCORE,
                target = 250,
            ),
            achievement(
                id = AchievementId.SCORE_500,
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.HIGHEST_SCORE,
                target = 500,
            ),
            achievement(
                id = AchievementId.TIME_MASTER,
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.LEVEL_FINISH_WITH_TIME_REMAINING,
                target = 1,
                timeRemainingThresholdSeconds = 45,
            ),
            achievement(
                id = AchievementId.COUNTRY_SLAYER,
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_CATEGORY,
                target = 1,
                requiredCategory = GameCategory.COUNTRIES,
            ),
            achievement(
                id = AchievementId.TONGUE_TAMER,
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_CATEGORY,
                target = 1,
                requiredCategory = GameCategory.LANGUAGES,
            ),
            achievement(
                id = AchievementId.CORPORATE_CURSE,
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_CATEGORY,
                target = 1,
                requiredCategory = GameCategory.COMPANIES,
            ),
            achievement(
                id = AchievementId.BEAST_BINDER,
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_CATEGORY,
                target = 1,
                requiredCategory = GameCategory.ANIMALS,
            ),
            achievement(
                id = AchievementId.WORLD_COMPLETIONIST,
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.UNIQUE_CATEGORIES_WON,
                target = GameCategory.entries.size,
            ),
            achievement(
                id = AchievementId.EASY_CLEARED,
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_DIFFICULTY,
                target = 1,
                requiredDifficulty = GameDifficulty.EASY,
            ),
            achievement(
                id = AchievementId.MEDIUM_CLEARED,
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_DIFFICULTY,
                target = 1,
                requiredDifficulty = GameDifficulty.MEDIUM,
            ),
            achievement(
                id = AchievementId.HARD_CLEARED,
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_DIFFICULTY,
                target = 1,
                requiredDifficulty = GameDifficulty.HARD,
            ),
            achievement(
                id = AchievementId.VERY_HARD_CLEARED,
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_DIFFICULTY,
                target = 1,
                requiredDifficulty = GameDifficulty.VERY_HARD,
            ),
            achievement(
                id = AchievementId.DIFFICULTY_MASTER,
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.UNIQUE_DIFFICULTIES_WON,
                target = GameDifficulty.entries.size,
            ),
            achievement(
                id = AchievementId.IRON_BONES,
                group = AchievementGroup.ENDURANCE,
                trigger = AchievementTrigger.SESSION_GAMES_PLAYED,
                target = 3,
            ),
            achievement(
                id = AchievementId.MIDNIGHT_MARATHON,
                group = AchievementGroup.ENDURANCE,
                trigger = AchievementTrigger.SESSION_GAMES_PLAYED,
                target = 5,
            ),
            achievement(
                id = AchievementId.GRINDSTONE,
                group = AchievementGroup.ENDURANCE,
                trigger = AchievementTrigger.LEVELS_COMPLETED_TOTAL,
                target = 10,
            ),
            achievement(
                id = AchievementId.BONE_MILL,
                group = AchievementGroup.ENDURANCE,
                trigger = AchievementTrigger.LEVELS_COMPLETED_TOTAL,
                target = 25,
            ),
            achievement(
                id = AchievementId.NIGHT_REAPER,
                group = AchievementGroup.ENDURANCE,
                trigger = AchievementTrigger.LEVELS_COMPLETED_TOTAL,
                target = 50,
            ),
            achievement(
                id = AchievementId.REVEAL_RESTRAINT,
                group = AchievementGroup.HINT_DISCIPLINE,
                trigger = AchievementTrigger.FULL_GAME_REVEAL_HINT_MAX,
                target = 1,
                maxAllowedHintUses = 1,
            ),
            achievement(
                id = AchievementId.ELIMINATE_RESTRAINT,
                group = AchievementGroup.HINT_DISCIPLINE,
                trigger = AchievementTrigger.FULL_GAME_ELIMINATE_HINT_MAX,
                target = 1,
                maxAllowedHintUses = 1,
            ),
            achievement(
                id = AchievementId.HINT_MINIMALIST,
                group = AchievementGroup.HINT_DISCIPLINE,
                trigger = AchievementTrigger.LOW_HINT_WINS_TOTAL,
                target = 3,
                maxAllowedHintUses = 1,
            ),
            achievement(
                id = AchievementId.COLD_TURKEY,
                group = AchievementGroup.HINT_DISCIPLINE,
                trigger = AchievementTrigger.WIN_WITHOUT_HINTS_TOTAL,
                target = 10,
            ),
            achievement(
                id = AchievementId.TIME_KEEPER,
                group = AchievementGroup.TIME_CONTROL,
                trigger = AchievementTrigger.LEVEL_FINISH_WITH_TIME_REMAINING,
                target = 3,
                timeRemainingThresholdSeconds = 30,
            ),
            achievement(
                id = AchievementId.CLOCK_BREAKER,
                group = AchievementGroup.TIME_CONTROL,
                trigger = AchievementTrigger.LEVEL_FINISH_WITH_TIME_REMAINING,
                target = 10,
                timeRemainingThresholdSeconds = 30,
            ),
            achievement(
                id = AchievementId.SANDGLASS_LORD,
                group = AchievementGroup.TIME_CONTROL,
                trigger = AchievementTrigger.LEVEL_FINISH_WITH_TIME_REMAINING,
                target = 5,
                timeRemainingThresholdSeconds = 45,
            ),
            achievement(
                id = AchievementId.ARCHIVIST,
                group = AchievementGroup.META,
                trigger = AchievementTrigger.HISTORY_ENTRIES_TOTAL,
                target = 20,
            ),
            achievement(
                id = AchievementId.CURSE_COLLECTOR,
                group = AchievementGroup.META,
                trigger = AchievementTrigger.ACHIEVEMENTS_UNLOCKED_TOTAL,
                target = 10,
            ),
        )
    }
}

private fun achievement(
    id: AchievementId,
    group: AchievementGroup,
    trigger: AchievementTrigger,
    target: Int,
    rarity: AchievementRarity = id.rarityFor(),
    requiredCategory: GameCategory? = null,
    requiredDifficulty: GameDifficulty? = null,
    timeRemainingThresholdSeconds: Int? = null,
    maxAllowedHintUses: Int? = null,
): AchievementDefinition {
    require(target > 0) {
        "Achievement target must be greater than 0 for id '$id'."
    }
    return AchievementDefinition(
        id = id,
        group = group,
        rarity = rarity,
        trigger = trigger,
        target = target,
        requiredCategory = requiredCategory,
        requiredDifficulty = requiredDifficulty,
        timeRemainingThresholdSeconds = timeRemainingThresholdSeconds,
        maxAllowedHintUses = maxAllowedHintUses,
    )
}
