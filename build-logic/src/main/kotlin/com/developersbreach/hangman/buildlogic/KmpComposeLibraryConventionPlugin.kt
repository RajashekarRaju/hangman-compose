package com.developersbreach.hangman.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class KmpComposeLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        pluginManager.apply("hangman.kmp.library")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        pluginManager.apply("org.jetbrains.compose")
    }
}
