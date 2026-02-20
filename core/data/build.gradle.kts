plugins {
    id("hangman.kmp.library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":game-core"))
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.koin.core)
            }
        }

        val jvmSharedMain by creating {
            dependsOn(commonMain)
            dependencies {
                api(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
            }
        }

        val androidMain by getting {
            dependsOn(jvmSharedMain)
        }

        val desktopMain by getting {
            dependsOn(jvmSharedMain)
        }

        val iosMain by creating {
            dependsOn(commonMain)
        }

        val iosArm64Main by getting {
            dependsOn(iosMain)
        }

        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }

        val wasmJsMain by getting {
            dependencies {
                implementation(libs.kotlinx.browser)
            }
        }
    }
}

android {
    namespace = "com.developersbreach.hangman.core.data"
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
