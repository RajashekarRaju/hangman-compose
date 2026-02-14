pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

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
include(":core:designsystem")
include(":core:data")
include(":feature:onboarding")
include(":feature:game")
include(":feature:history")
