package showcase.convention

import showcase.util.libs
import showcase.util.library

plugins {
    id("showcase.convention.kmp-module")
    id("showcase.primitive.compose")
    id("showcase.primitive.kmp.compose")
    id("showcase.primitive.logging")
    id("showcase.primitive.unit-test")
    id("org.jetbrains.kotlin.plugin.serialization")
}

kotlin {
    sourceSets {
        all {
            languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
        }

        commonMain {
            dependencies {
                // Core module dependencies
                implementation(project(":core:foundation"))
                implementation(project(":core:data"))
                api(project(":core:ui"))

                // Compose Multiplatform base dependencies
                val compose =
                    project.extensions
                        .getByType(org.jetbrains.compose.ComposeExtension::class.java)
                        .dependencies
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)

                // Jetpack ViewModel (KMP)
                implementation(libs.library("lifecycleViewModel"))

                // Serialization
                implementation(libs.library("kotlinxSerializationJson"))
            }
        }

        findByName("androidMain")?.dependencies {
            implementation(libs.library("composeUiTooling"))
        }

        // iosMain previously pulled in Koin by default. After Stage 4 of
        // the Metro DI migration, features no longer depend on Koin on
        // iOS — the single `IosAppGraph` in the `shared` module is the
        // sole DI entry point and resolves per-feature view models
        // through Metro's compiler-plugin-generated implementation.
    }
}
