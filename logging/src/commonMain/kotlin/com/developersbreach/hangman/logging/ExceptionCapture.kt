package com.developersbreach.hangman.logging

inline fun <T> runCatchingLogged(
    tag: String,
    message: () -> String,
    block: () -> T,
): Result<T> {
    return runCatching(block).onFailure { throwable ->
        Log.e(tag, throwable, message)
    }
}

inline fun <T> runCatchingLogged(
    tag: String,
    block: () -> T,
): Result<T> {
    return runCatchingLogged(
        tag = tag,
        message = { "Operation failed." },
        block = block,
    )
}

fun logCaughtException(
    tag: String,
    throwable: Throwable,
    message: () -> String = { "Operation failed." },
) {
    Log.e(tag, throwable, message)
}
