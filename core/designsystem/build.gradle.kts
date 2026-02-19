plugins {
    id("hangman.kmp.compose-library")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.developersbreach.hangman.core.designsystem.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.material3)
            implementation(libs.jetbrains.compose.material.icons.extended)
            implementation(libs.compose.components.resources)
        }
    }
}

android {
    namespace = "com.developersbreach.hangman.core.designsystem"
}
