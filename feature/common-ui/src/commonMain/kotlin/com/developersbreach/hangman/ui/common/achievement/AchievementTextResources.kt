package com.developersbreach.hangman.ui.common.achievement

import com.developersbreach.game.core.achievements.AchievementId
import com.developersbreach.hangman.feature.common.ui.generated.resources.Res
import com.developersbreach.hangman.feature.common.ui.generated.resources.*
import org.jetbrains.compose.resources.StringResource

fun AchievementId.titleRes(): StringResource {
    return when (this) {
        AchievementId.FIRST_BLOOD -> Res.string.achievement_title_first_blood
        AchievementId.GRAVE_WALKER -> Res.string.achievement_title_grave_walker
        AchievementId.NIGHT_SHIFT -> Res.string.achievement_title_night_shift
        AchievementId.ETERNAL_PLAYER -> Res.string.achievement_title_eternal_player
        AchievementId.FIRST_WIN -> Res.string.achievement_title_first_win
        AchievementId.WIN_COLLECTOR -> Res.string.achievement_title_win_collector
        AchievementId.CROWN_OF_ASH -> Res.string.achievement_title_crown_of_ash
        AchievementId.UNBROKEN -> Res.string.achievement_title_unbroken
        AchievementId.RELENTLESS -> Res.string.achievement_title_relentless
        AchievementId.IMMORTAL_STREAK -> Res.string.achievement_title_immortal_streak
        AchievementId.NO_CRUTCHES -> Res.string.achievement_title_no_crutches
        AchievementId.MIND_READER -> Res.string.achievement_title_mind_reader
        AchievementId.PERFECT_HUNT -> Res.string.achievement_title_perfect_hunt
        AchievementId.SURGEON -> Res.string.achievement_title_surgeon
        AchievementId.SCORE_100 -> Res.string.achievement_title_score_100
        AchievementId.SCORE_250 -> Res.string.achievement_title_score_250
        AchievementId.SCORE_500 -> Res.string.achievement_title_score_500
        AchievementId.TIME_MASTER -> Res.string.achievement_title_time_master
        AchievementId.COUNTRY_SLAYER -> Res.string.achievement_title_country_slayer
        AchievementId.TONGUE_TAMER -> Res.string.achievement_title_tongue_tamer
        AchievementId.CORPORATE_CURSE -> Res.string.achievement_title_corporate_curse
        AchievementId.BEAST_BINDER -> Res.string.achievement_title_beast_binder
        AchievementId.WORLD_COMPLETIONIST -> Res.string.achievement_title_world_completionist
        AchievementId.EASY_CLEARED -> Res.string.achievement_title_easy_cleared
        AchievementId.MEDIUM_CLEARED -> Res.string.achievement_title_medium_cleared
        AchievementId.HARD_CLEARED -> Res.string.achievement_title_hard_cleared
        AchievementId.VERY_HARD_CLEARED -> Res.string.achievement_title_very_hard_cleared
        AchievementId.DIFFICULTY_MASTER -> Res.string.achievement_title_difficulty_master
        AchievementId.IRON_BONES -> Res.string.achievement_title_iron_bones
        AchievementId.MIDNIGHT_MARATHON -> Res.string.achievement_title_midnight_marathon
        AchievementId.GRINDSTONE -> Res.string.achievement_title_grindstone
        AchievementId.BONE_MILL -> Res.string.achievement_title_bone_mill
        AchievementId.NIGHT_REAPER -> Res.string.achievement_title_night_reaper
        AchievementId.REVEAL_RESTRAINT -> Res.string.achievement_title_reveal_restraint
        AchievementId.ELIMINATE_RESTRAINT -> Res.string.achievement_title_eliminate_restraint
        AchievementId.HINT_MINIMALIST -> Res.string.achievement_title_hint_minimalist
        AchievementId.COLD_TURKEY -> Res.string.achievement_title_cold_turkey
        AchievementId.TIME_KEEPER -> Res.string.achievement_title_time_keeper
        AchievementId.CLOCK_BREAKER -> Res.string.achievement_title_clock_breaker
        AchievementId.SANDGLASS_LORD -> Res.string.achievement_title_sandglass_lord
        AchievementId.ARCHIVIST -> Res.string.achievement_title_archivist
        AchievementId.CURSE_COLLECTOR -> Res.string.achievement_title_curse_collector
    }
}

fun AchievementId.descriptionRes(): StringResource {
    return when (this) {
        AchievementId.FIRST_BLOOD -> Res.string.achievement_description_first_blood
        AchievementId.GRAVE_WALKER -> Res.string.achievement_description_grave_walker
        AchievementId.NIGHT_SHIFT -> Res.string.achievement_description_night_shift
        AchievementId.ETERNAL_PLAYER -> Res.string.achievement_description_eternal_player
        AchievementId.FIRST_WIN -> Res.string.achievement_description_first_win
        AchievementId.WIN_COLLECTOR -> Res.string.achievement_description_win_collector
        AchievementId.CROWN_OF_ASH -> Res.string.achievement_description_crown_of_ash
        AchievementId.UNBROKEN -> Res.string.achievement_description_unbroken
        AchievementId.RELENTLESS -> Res.string.achievement_description_relentless
        AchievementId.IMMORTAL_STREAK -> Res.string.achievement_description_immortal_streak
        AchievementId.NO_CRUTCHES -> Res.string.achievement_description_no_crutches
        AchievementId.MIND_READER -> Res.string.achievement_description_mind_reader
        AchievementId.PERFECT_HUNT -> Res.string.achievement_description_perfect_hunt
        AchievementId.SURGEON -> Res.string.achievement_description_surgeon
        AchievementId.SCORE_100 -> Res.string.achievement_description_score_100
        AchievementId.SCORE_250 -> Res.string.achievement_description_score_250
        AchievementId.SCORE_500 -> Res.string.achievement_description_score_500
        AchievementId.TIME_MASTER -> Res.string.achievement_description_time_master
        AchievementId.COUNTRY_SLAYER -> Res.string.achievement_description_country_slayer
        AchievementId.TONGUE_TAMER -> Res.string.achievement_description_tongue_tamer
        AchievementId.CORPORATE_CURSE -> Res.string.achievement_description_corporate_curse
        AchievementId.BEAST_BINDER -> Res.string.achievement_description_beast_binder
        AchievementId.WORLD_COMPLETIONIST -> Res.string.achievement_description_world_completionist
        AchievementId.EASY_CLEARED -> Res.string.achievement_description_easy_cleared
        AchievementId.MEDIUM_CLEARED -> Res.string.achievement_description_medium_cleared
        AchievementId.HARD_CLEARED -> Res.string.achievement_description_hard_cleared
        AchievementId.VERY_HARD_CLEARED -> Res.string.achievement_description_very_hard_cleared
        AchievementId.DIFFICULTY_MASTER -> Res.string.achievement_description_difficulty_master
        AchievementId.IRON_BONES -> Res.string.achievement_description_iron_bones
        AchievementId.MIDNIGHT_MARATHON -> Res.string.achievement_description_midnight_marathon
        AchievementId.GRINDSTONE -> Res.string.achievement_description_grindstone
        AchievementId.BONE_MILL -> Res.string.achievement_description_bone_mill
        AchievementId.NIGHT_REAPER -> Res.string.achievement_description_night_reaper
        AchievementId.REVEAL_RESTRAINT -> Res.string.achievement_description_reveal_restraint
        AchievementId.ELIMINATE_RESTRAINT -> Res.string.achievement_description_eliminate_restraint
        AchievementId.HINT_MINIMALIST -> Res.string.achievement_description_hint_minimalist
        AchievementId.COLD_TURKEY -> Res.string.achievement_description_cold_turkey
        AchievementId.TIME_KEEPER -> Res.string.achievement_description_time_keeper
        AchievementId.CLOCK_BREAKER -> Res.string.achievement_description_clock_breaker
        AchievementId.SANDGLASS_LORD -> Res.string.achievement_description_sandglass_lord
        AchievementId.ARCHIVIST -> Res.string.achievement_description_archivist
        AchievementId.CURSE_COLLECTOR -> Res.string.achievement_description_curse_collector
    }
}
