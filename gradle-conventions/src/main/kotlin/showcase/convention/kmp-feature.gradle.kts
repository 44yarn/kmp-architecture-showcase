package showcase.convention

import showcase.util.libs
import showcase.util.library

plugins {
    id("showcase.convention.kmp-module")
    id("showcase.primitive.compose")
    id("showcase.primitive.kmp.compose")
    id("showcase.primitive.hilt")
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
                // Core モジュール依存
                implementation(project(":core:foundation"))
                implementation(project(":core:data"))
                api(project(":core:ui-kit"))

                // Compose Multiplatform 基本依存
                val compose =
                    project.extensions
                        .getByType(org.jetbrains.compose.ComposeExtension::class.java)
                        .dependencies
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)

                // KMP-NativeCoroutines
                implementation(libs.library("nativecoroutinesCore"))

                // KMP-ObservableViewModel
                implementation(libs.library("kmpObservableViewModelCore"))

                // Serialization
                implementation(libs.library("kotlinxSerializationJson"))
            }
        }

        findByName("androidMain")?.dependencies {
            implementation(libs.library("composeUiTooling"))
        }

        findByName("iosMain")?.dependencies {
            implementation(libs.library("koinCore"))
        }
    }
}
