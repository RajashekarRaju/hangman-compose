import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("hangman.kmp.compose-library")
}

apply(from = rootProject.file("gradle/sentry-dsn.gradle.kts"))

val sentryDsn = extra["hangmanSentryDsn"] as String

val generateWasmSentryConfig by tasks.registering {
    val outputDir = layout.buildDirectory.dir("generated/wasmSentryConfig")
    outputs.dir(outputDir)
    doLast {
        val file = outputDir.get().file("hangman-sentry-config.js").asFile
        file.parentFile.mkdirs()
        val escapedDsn = sentryDsn
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
        file.writeText(
            """
                window.HANGMAN_SENTRY_DSN = "$escapedDsn";
            """.trimIndent(),
        )
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.developersbreach.hangman.composeapp.generated.resources"
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
            implementation(project(":logging"))
            implementation(project(":navigation"))
            implementation(project(":core:designsystem"))
            implementation(project(":core:data"))
            implementation(project(":feature:mainmenu"))
            implementation(project(":feature:settings"))
            implementation(project(":feature:common-ui"))
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
        wasmJsMain.resources.srcDir(layout.buildDirectory.dir("generated/wasmSentryConfig"))
        wasmJsMain.resources.srcDir(project(":core:data").projectDir.resolve(
            "src/androidMain/res/raw")
        )
        wasmJsMain.dependencies {

        }
    }
}

tasks.matching { task ->
    task.name.contains("WasmJs", ignoreCase = true) && task.name.contains("Resources", ignoreCase = true)
}.configureEach {
    dependsOn(generateWasmSentryConfig)
}

android {
    namespace = "com.developersbreach.hangman.composeapp"
}

compose.desktop {
    application {
        mainClass = "com.developersbreach.hangman.composeapp.DesktopMainKt"
        if (sentryDsn.isNotBlank()) {
            jvmArgs("-Dhangman.sentry.dsn=$sentryDsn")
        }

        buildTypes {
            release {
                proguard {
                    isEnabled.set(false)
                }
            }
        }

        nativeDistributions {
            packageName = "Hangman"
            packageVersion = "1.0.3"
            description = "Hangman"
            vendor = "Developers Breach"

            macOS {
                packageVersion = "1.0.3"
                dmgPackageVersion = "1.0.3"
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
