package com.developersbreach.hangman.logging

import platform.Foundation.NSLog

actual fun platformLog(event: LogEvent) {
    NSLog("%@", formatLogMessage(event))
}
