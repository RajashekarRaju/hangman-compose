plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle:9.0.1")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.3.10")
    implementation("org.jetbrains.compose:org.jetbrains.compose.gradle.plugin:1.10.1")
}

gradlePlugin {
    plugins {
        create("hangmanKmpLibrary") {
            id = "hangman.kmp.library"
            implementationClass = "com.developersbreach.hangman.buildlogic.KmpLibraryConventionPlugin"
        }
        create("hangmanKmpComposeLibrary") {
            id = "hangman.kmp.compose-library"
            implementationClass = "com.developersbreach.hangman.buildlogic.KmpComposeLibraryConventionPlugin"
        }
    }
}
