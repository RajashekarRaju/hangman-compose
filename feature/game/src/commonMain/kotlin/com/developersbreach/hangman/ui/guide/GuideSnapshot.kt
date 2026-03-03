package com.developersbreach.hangman.ui.guide

import com.developersbreach.game.core.MAX_ATTEMPTS_PER_LEVEL
import com.developersbreach.hangman.feature.game.generated.resources.Res
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_snapshot_a_desc
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_snapshot_b_desc
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_snapshot_c_desc
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_snapshot_d_desc
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_snapshot_e_desc
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_snapshot_f_desc
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_snapshot_g_desc
import com.developersbreach.hangman.feature.game.generated.resources.game_guide_snapshot_h_desc
import org.jetbrains.compose.resources.StringResource

internal data class GuideSnapshot(
    val stage: Int,
    val descriptionRes: StringResource,
    val attemptsLeft: Int,
)

private val stageDescriptions = listOf(
    Res.string.game_guide_snapshot_a_desc,
    Res.string.game_guide_snapshot_b_desc,
    Res.string.game_guide_snapshot_c_desc,
    Res.string.game_guide_snapshot_d_desc,
    Res.string.game_guide_snapshot_e_desc,
    Res.string.game_guide_snapshot_f_desc,
    Res.string.game_guide_snapshot_g_desc,
    Res.string.game_guide_snapshot_h_desc,
)

internal val stageSnapshots = stageDescriptions.mapIndexed { index, descriptionRes ->
    GuideSnapshot(
        stage = index + 1,
        descriptionRes = descriptionRes,
        attemptsLeft = MAX_ATTEMPTS_PER_LEVEL - index,
    )
}