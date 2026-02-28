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
                title = "First Blood",
                description = "Play your first game.",
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_PLAYED_TOTAL,
                target = 1,
            ),
            achievement(
                id = AchievementId.GRAVE_WALKER,
                title = "Grave Walker",
                description = "Play 10 games.",
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_PLAYED_TOTAL,
                target = 10,
            ),
            achievement(
                id = AchievementId.NIGHT_SHIFT,
                title = "Night Shift",
                description = "Play 25 games.",
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_PLAYED_TOTAL,
                target = 25,
            ),
            achievement(
                id = AchievementId.ETERNAL_PLAYER,
                title = "Eternal Player",
                description = "Play 50 games.",
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_PLAYED_TOTAL,
                target = 50,
            ),
            achievement(
                id = AchievementId.FIRST_WIN,
                title = "First Win",
                description = "Win your first game.",
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_WON_TOTAL,
                target = 1,
            ),
            achievement(
                id = AchievementId.WIN_COLLECTOR,
                title = "Win Collector",
                description = "Win 10 games.",
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_WON_TOTAL,
                target = 10,
            ),
            achievement(
                id = AchievementId.CROWN_OF_ASH,
                title = "Crown Of Ash",
                description = "Win 25 games.",
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.GAMES_WON_TOTAL,
                target = 25,
            ),
            achievement(
                id = AchievementId.UNBROKEN,
                title = "Unbroken",
                description = "Reach a 3-game win streak.",
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.BEST_WIN_STREAK,
                target = 3,
            ),
            achievement(
                id = AchievementId.RELENTLESS,
                title = "Relentless",
                description = "Reach a 5-game win streak.",
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.BEST_WIN_STREAK,
                target = 5,
            ),
            achievement(
                id = AchievementId.IMMORTAL_STREAK,
                title = "Immortal Streak",
                description = "Reach a 10-game win streak.",
                group = AchievementGroup.PROGRESS,
                trigger = AchievementTrigger.BEST_WIN_STREAK,
                target = 10,
            ),
            achievement(
                id = AchievementId.NO_CRUTCHES,
                title = "No Crutches",
                description = "Win a game without hints.",
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.WIN_WITHOUT_HINTS_TOTAL,
                target = 1,
            ),
            achievement(
                id = AchievementId.MIND_READER,
                title = "Mind Reader",
                description = "Win 5 games without hints.",
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.WIN_WITHOUT_HINTS_TOTAL,
                target = 5,
            ),
            achievement(
                id = AchievementId.PERFECT_HUNT,
                title = "Perfect Hunt",
                description = "Win a game with all attempts remaining.",
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.PERFECT_WINS_TOTAL,
                target = 1,
            ),
            achievement(
                id = AchievementId.SURGEON,
                title = "Surgeon",
                description = "Win 5 perfect games.",
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.PERFECT_WINS_TOTAL,
                target = 5,
            ),
            achievement(
                id = AchievementId.SCORE_100,
                title = "Score 100",
                description = "Reach score 100 in any game.",
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.HIGHEST_SCORE,
                target = 100,
            ),
            achievement(
                id = AchievementId.SCORE_250,
                title = "Score 250",
                description = "Reach score 250 in any game.",
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.HIGHEST_SCORE,
                target = 250,
            ),
            achievement(
                id = AchievementId.SCORE_500,
                title = "Score 500",
                description = "Reach score 500 in any game.",
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.HIGHEST_SCORE,
                target = 500,
            ),
            achievement(
                id = AchievementId.TIME_MASTER,
                title = "Time Master",
                description = "Finish a level with at least 45 seconds left.",
                group = AchievementGroup.SKILL,
                trigger = AchievementTrigger.LEVEL_FINISH_WITH_TIME_REMAINING,
                target = 1,
                timeRemainingThresholdSeconds = 45,
            ),
            achievement(
                id = AchievementId.COUNTRY_SLAYER,
                title = "Country Slayer",
                description = "Win once in Countries.",
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_CATEGORY,
                target = 1,
                requiredCategory = GameCategory.COUNTRIES,
            ),
            achievement(
                id = AchievementId.TONGUE_TAMER,
                title = "Tongue Tamer",
                description = "Win once in Languages.",
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_CATEGORY,
                target = 1,
                requiredCategory = GameCategory.LANGUAGES,
            ),
            achievement(
                id = AchievementId.CORPORATE_CURSE,
                title = "Corporate Curse",
                description = "Win once in Companies.",
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_CATEGORY,
                target = 1,
                requiredCategory = GameCategory.COMPANIES,
            ),
            achievement(
                id = AchievementId.BEAST_BINDER,
                title = "Beast Binder",
                description = "Win once in Animals.",
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_CATEGORY,
                target = 1,
                requiredCategory = GameCategory.ANIMALS,
            ),
            achievement(
                id = AchievementId.WORLD_COMPLETIONIST,
                title = "World Completionist",
                description = "Win in all categories at least once.",
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.UNIQUE_CATEGORIES_WON,
                target = GameCategory.entries.size,
            ),
            achievement(
                id = AchievementId.EASY_CLEARED,
                title = "Easy Cleared",
                description = "Win once on Easy.",
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_DIFFICULTY,
                target = 1,
                requiredDifficulty = GameDifficulty.EASY,
            ),
            achievement(
                id = AchievementId.MEDIUM_CLEARED,
                title = "Medium Cleared",
                description = "Win once on Medium.",
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_DIFFICULTY,
                target = 1,
                requiredDifficulty = GameDifficulty.MEDIUM,
            ),
            achievement(
                id = AchievementId.HARD_CLEARED,
                title = "Hard Cleared",
                description = "Win once on Hard.",
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_DIFFICULTY,
                target = 1,
                requiredDifficulty = GameDifficulty.HARD,
            ),
            achievement(
                id = AchievementId.VERY_HARD_CLEARED,
                title = "Very Hard Cleared",
                description = "Win once on Very Hard.",
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.WIN_IN_DIFFICULTY,
                target = 1,
                requiredDifficulty = GameDifficulty.VERY_HARD,
            ),
            achievement(
                id = AchievementId.DIFFICULTY_MASTER,
                title = "Difficulty Master",
                description = "Win in all difficulties at least once.",
                group = AchievementGroup.COLLECTION,
                trigger = AchievementTrigger.UNIQUE_DIFFICULTIES_WON,
                target = GameDifficulty.entries.size,
            ),
            achievement(
                id = AchievementId.IRON_BONES,
                title = "Iron Bones",
                description = "Play 3 games in a single app session.",
                group = AchievementGroup.ENDURANCE,
                trigger = AchievementTrigger.SESSION_GAMES_PLAYED,
                target = 3,
            ),
            achievement(
                id = AchievementId.MIDNIGHT_MARATHON,
                title = "Midnight Marathon",
                description = "Play 5 games in a single app session.",
                group = AchievementGroup.ENDURANCE,
                trigger = AchievementTrigger.SESSION_GAMES_PLAYED,
                target = 5,
            ),
            achievement(
                id = AchievementId.GRINDSTONE,
                title = "Grindstone",
                description = "Complete 10 levels.",
                group = AchievementGroup.ENDURANCE,
                trigger = AchievementTrigger.LEVELS_COMPLETED_TOTAL,
                target = 10,
            ),
            achievement(
                id = AchievementId.BONE_MILL,
                title = "Bone Mill",
                description = "Complete 25 levels.",
                group = AchievementGroup.ENDURANCE,
                trigger = AchievementTrigger.LEVELS_COMPLETED_TOTAL,
                target = 25,
            ),
            achievement(
                id = AchievementId.NIGHT_REAPER,
                title = "Night Reaper",
                description = "Complete 50 levels.",
                group = AchievementGroup.ENDURANCE,
                trigger = AchievementTrigger.LEVELS_COMPLETED_TOTAL,
                target = 50,
            ),
            achievement(
                id = AchievementId.REVEAL_RESTRAINT,
                title = "Reveal Restraint",
                description = "Win a full game using Reveal at most once.",
                group = AchievementGroup.HINT_DISCIPLINE,
                trigger = AchievementTrigger.FULL_GAME_REVEAL_HINT_MAX,
                target = 1,
                maxAllowedHintUses = 1,
            ),
            achievement(
                id = AchievementId.ELIMINATE_RESTRAINT,
                title = "Eliminate Restraint",
                description = "Win a full game using Eliminate at most once.",
                group = AchievementGroup.HINT_DISCIPLINE,
                trigger = AchievementTrigger.FULL_GAME_ELIMINATE_HINT_MAX,
                target = 1,
                maxAllowedHintUses = 1,
            ),
            achievement(
                id = AchievementId.HINT_MINIMALIST,
                title = "Hint Minimalist",
                description = "Win 3 games using at most one hint per game.",
                group = AchievementGroup.HINT_DISCIPLINE,
                trigger = AchievementTrigger.LOW_HINT_WINS_TOTAL,
                target = 3,
                maxAllowedHintUses = 1,
            ),
            achievement(
                id = AchievementId.COLD_TURKEY,
                title = "Cold Turkey",
                description = "Win 10 games with no hints.",
                group = AchievementGroup.HINT_DISCIPLINE,
                trigger = AchievementTrigger.WIN_WITHOUT_HINTS_TOTAL,
                target = 10,
            ),
            achievement(
                id = AchievementId.TIME_KEEPER,
                title = "Time Keeper",
                description = "Finish 3 levels with at least 30 seconds left.",
                group = AchievementGroup.TIME_CONTROL,
                trigger = AchievementTrigger.LEVEL_FINISH_WITH_TIME_REMAINING,
                target = 3,
                timeRemainingThresholdSeconds = 30,
            ),
            achievement(
                id = AchievementId.CLOCK_BREAKER,
                title = "Clock Breaker",
                description = "Finish 10 levels with at least 30 seconds left.",
                group = AchievementGroup.TIME_CONTROL,
                trigger = AchievementTrigger.LEVEL_FINISH_WITH_TIME_REMAINING,
                target = 10,
                timeRemainingThresholdSeconds = 30,
            ),
            achievement(
                id = AchievementId.SANDGLASS_LORD,
                title = "Sandglass Lord",
                description = "Finish 5 levels with at least 45 seconds left.",
                group = AchievementGroup.TIME_CONTROL,
                trigger = AchievementTrigger.LEVEL_FINISH_WITH_TIME_REMAINING,
                target = 5,
                timeRemainingThresholdSeconds = 45,
            ),
            achievement(
                id = AchievementId.ARCHIVIST,
                title = "Archivist",
                description = "Record 20 history entries.",
                group = AchievementGroup.META,
                trigger = AchievementTrigger.HISTORY_ENTRIES_TOTAL,
                target = 20,
            ),
            achievement(
                id = AchievementId.CURSE_COLLECTOR,
                title = "Curse Collector",
                description = "Unlock 10 achievements.",
                group = AchievementGroup.META,
                trigger = AchievementTrigger.ACHIEVEMENTS_UNLOCKED_TOTAL,
                target = 10,
            ),
        )
    }
}

private fun achievement(
    id: AchievementId,
    title: String,
    description: String,
    group: AchievementGroup,
    trigger: AchievementTrigger,
    target: Int,
    requiredCategory: GameCategory? = null,
    requiredDifficulty: GameDifficulty? = null,
    timeRemainingThresholdSeconds: Int? = null,
    maxAllowedHintUses: Int? = null,
): AchievementDefinition {
    require(title.isNotBlank()) {
        "Achievement title must not be blank for id '$id'."
    }
    require(description.isNotBlank()) {
        "Achievement description must not be blank for id '$id'."
    }
    require(target > 0) {
        "Achievement target must be greater than 0 for id '$id'."
    }
    return AchievementDefinition(
        id = id,
        title = title,
        description = description,
        group = group,
        trigger = trigger,
        target = target,
        requiredCategory = requiredCategory,
        requiredDifficulty = requiredDifficulty,
        timeRemainingThresholdSeconds = timeRemainingThresholdSeconds,
        maxAllowedHintUses = maxAllowedHintUses,
    )
}
