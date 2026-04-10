package showcase.primitive

import com.android.build.gradle.LibraryExtension
import showcase.util.libs
import showcase.util.library
import showcase.util.version
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * KMP Compose configuration plugin.
 *
 * Manages the configuration for both Compose Multiplatform and
 * Android Compose in KMP modules.
 */
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

// KMP Compose configuration.
extensions.configure<KotlinMultiplatformExtension> {
    sourceSets.apply {
        getByName("commonMain") {
            dependencies {
                // Basic Compose Multiplatform dependencies are defined in each module's build.gradle.kts
            }
        }

        findByName("androidMain")?.dependencies {
            implementation(libs.library("navigationCompose"))
            implementation(libs.library("lifecycleRuntimeCompose"))
            implementation(libs.library("lifecycleViewmodelCompose"))
            implementation(libs.library("activityCompose"))
        }
    }
}

// Android Compose configuration.
extensions.findByType(LibraryExtension::class.java)?.apply {
    buildFeatures {
        compose = true
    }
}
