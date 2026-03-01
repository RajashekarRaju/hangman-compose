package com.developersbreach.hangman.ui.game

import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.time

@OptIn(ExperimentalForeignApi::class)
internal actual fun nowEpochMillis(): Long = time(null).toLong() * 1_000L
