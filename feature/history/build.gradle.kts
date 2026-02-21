plugins {
    id("hangman.kmp.compose-library")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.developersbreach.hangman.feature.history.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":game-core"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:data"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.jetbrains.compose.material.icons.extended)
            implementation(libs.compose.components.resources)

            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}

dependencies {
    add("debugImplementation", libs.compose.ui.tooling)
}

android {
    namespace = "com.developersbreach.hangman.feature.history"
}
