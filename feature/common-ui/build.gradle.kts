plugins {
    id("hangman.kmp.compose-library")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.developersbreach.hangman.feature.common.ui.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":game-core"))
            implementation(project(":logging"))
            implementation(project(":core:designsystem"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "com.developersbreach.hangman.feature.common.ui"
}
