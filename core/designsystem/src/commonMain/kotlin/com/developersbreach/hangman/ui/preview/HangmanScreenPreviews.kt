package com.developersbreach.hangman.ui.preview

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Wide", showBackground = true,  device = Devices.DESKTOP)
@Preview(name = "Tablet", showBackground = true,  device = Devices.TABLET)
@Preview(name = "Mobile", showBackground = true, device = Devices.PIXEL_6A)
@Preview(name = "Fold", showBackground = true, device = Devices.PIXEL_FOLD)
@Preview(name = "Mobile", showBackground = true, device = Devices.PIXEL_3A)
annotation class HangmanScreenPreviews
