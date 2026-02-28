pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

includeBuild("build-logic")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Hangman"
include(":app")
include(":game-core")
include(":composeApp")
include(":navigation")
include(":core:designsystem")
include(":core:data")
include(":feature:onboarding")
include(":feature:game")
include(":feature:history")
include(":feature:common-ui")
include(":feature:achievements")
