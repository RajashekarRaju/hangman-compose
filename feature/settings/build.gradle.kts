plugins {
    id("hangman.kmp.compose-library")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":game-core"))
            implementation(project(":logging"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:data"))
            implementation(project(":feature:common-ui"))
            implementation(project(":feature:game"))
            implementation(project(":feature:mainmenu"))

            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.jetbrains.compose.material.icons.extended)
            implementation(libs.compose.components.resources)

            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.koin.compose.viewmodel)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

android {
    namespace = "com.developersbreach.hangman.feature.settings"
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
}
