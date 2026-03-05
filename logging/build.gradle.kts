plugins {
    id("hangman.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.sentry.kmp)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.developersbreach.hangman.logging"
}
