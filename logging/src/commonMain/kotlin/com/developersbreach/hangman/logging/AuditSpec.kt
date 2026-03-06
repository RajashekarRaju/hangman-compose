package com.developersbreach.hangman.logging

data class AuditSpec(
    val eventType: String,
    val parameters: Map<String, String> = emptyMap(),
)
