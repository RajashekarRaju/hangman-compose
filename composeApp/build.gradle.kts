import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("hangman.kmp.compose-library")
}

kotlin {
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")

    targets
        .withType<KotlinNativeTarget>()
        .matching { it.konanTarget.family.isAppleFamily }
        .configureEach {
            binaries.framework {
                baseName = "ComposeApp"
                isStatic = true
                binaryOption("bundleId", "com.developersbreach.hangman.composeapp")
            }
        }

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(project(":game-core"))
            implementation(project(":navigation"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:data"))
            implementation(project(":feature:onboarding"))
            implementation(project(":feature:game"))
            implementation(project(":feature:history"))
            implementation(project(":feature:achievements"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.components.resources)
            implementation(libs.koin.compose.viewmodel)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }

        val wasmJsMain by getting
        wasmJsMain.resources.srcDir(project(":core:data").projectDir.resolve(
            "src/androidMain/res/raw")
        )
        wasmJsMain.dependencies {

        }
    }
}

android {
    namespace = "com.developersbreach.hangman.composeapp"
}

compose.desktop {
    application {
        mainClass = "com.developersbreach.hangman.composeapp.DesktopMainKt"

        buildTypes {
            release {
                proguard {
                    isEnabled.set(false)
                }
            }
        }

        nativeDistributions {
            packageName = "Hangman"
            packageVersion = "1.0.2"
            description = "Hangman"
            vendor = "Developers Breach"

            macOS {
                packageVersion = "1.0.2"
                dmgPackageVersion = "1.0.2"
            }

            modules("java.naming")
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Rpm,
            )
        }
    }
}
