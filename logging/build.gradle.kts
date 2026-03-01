plugins {
    id("hangman.kmp.library")
}

kotlin {
    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.developersbreach.hangman.logging"
}
