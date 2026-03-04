package com.developersbreach.hangman.ui.settings

import com.developersbreach.hangman.feature.mainmenu.generated.resources.Res
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_cursor_bone
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_cursor_default
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_cursor_demon
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_cursor_hand
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_cursor_hand_bones
import com.developersbreach.hangman.feature.mainmenu.generated.resources.settings_cursor_skull
import com.developersbreach.hangman.repository.CursorStyle
import org.jetbrains.compose.resources.StringResource

internal fun CursorStyle.labelRes(): StringResource {
    return when (this) {
        CursorStyle.DEFAULT -> Res.string.settings_cursor_default
        CursorStyle.SKULL -> Res.string.settings_cursor_skull
        CursorStyle.DEMON -> Res.string.settings_cursor_demon
        CursorStyle.HAND -> Res.string.settings_cursor_hand
        CursorStyle.HAND_BONES -> Res.string.settings_cursor_hand_bones
        CursorStyle.BONE -> Res.string.settings_cursor_bone
    }
}
