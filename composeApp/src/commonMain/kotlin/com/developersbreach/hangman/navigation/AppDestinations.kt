package com.developersbreach.hangman.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination

@Serializable
data object OnBoardingRoute : Destination

@Serializable
data object GameRoute : Destination

@Serializable
data object HistoryRoute : Destination
