plugins {
    id("hangman.kmp.compose-library")
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")

    sourceSets {
        commonMain.dependencies {
            implementation(project(":feature:onboarding"))
            implementation(project(":feature:game"))
            implementation(project(":feature:history"))
            implementation(libs.compose.runtime)
            implementation(libs.jetbrains.navigation)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}

android {
    namespace = "com.developersbreach.hangman.navigation"
}
