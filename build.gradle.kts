// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.detekt) apply false
}

subprojects {
    pluginManager.withPlugin("org.jetbrains.kotlin.android") {
        apply(plugin = "io.gitlab.arturbosch.detekt")
    }
    pluginManager.withPlugin("org.jetbrains.kotlin.jvm") {
        apply(plugin = "io.gitlab.arturbosch.detekt")
    }

    plugins.withId("io.gitlab.arturbosch.detekt") {
        extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension>("detekt") {
            buildUponDefaultConfig = true
            allRules = false
            config = files("$rootDir/config/detekt/detekt.yml")
            autoCorrect = true
            basePath = rootDir.absolutePath

            source = files(
                "src/main/kotlin",
                "src/main/java",
                "src/test/kotlin",
                "src/androidTest/kotlin"
            )
        }

        dependencies {
            "detektPlugins"(libs.detektFormatting)
        }
    }
}