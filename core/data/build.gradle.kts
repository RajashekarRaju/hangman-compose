plugins {
    id("hangman.kmp.library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":game-core"))
                implementation(libs.kotlinx.coroutines.core)
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
