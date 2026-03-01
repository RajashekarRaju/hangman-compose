plugins {
    id("hangman.kmp.library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":logging"))
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

android {
    namespace = "com.developersbreach.game.core"
}
