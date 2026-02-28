plugins {
    id("hangman.kmp.compose-library")
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.developersbreach.hangman.feature.achievements.generated.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":game-core"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:data"))
            implementation(project(":feature:common-ui"))

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

dependencies {
    add("debugImplementation", libs.compose.ui.tooling)
}

android {
    namespace = "com.developersbreach.hangman.feature.achievements"
}
