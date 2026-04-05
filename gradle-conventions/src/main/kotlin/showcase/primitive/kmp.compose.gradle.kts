package showcase.primitive

import com.android.build.gradle.LibraryExtension
import showcase.util.libs
import showcase.util.library
import showcase.util.version
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * KMP Compose設定プラグイン
 * Compose MultiplatformとAndroid Composeの設定を管理
 */
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

// KMP Compose設定
extensions.configure<KotlinMultiplatformExtension> {
    sourceSets.apply {
        getByName("commonMain") {
            dependencies {
                // Basic Compose Multiplatform dependencies are defined in individual module's build.gradle.kts
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

// Android Compose設定
extensions.findByType(LibraryExtension::class.java)?.apply {
    buildFeatures {
        compose = true
    }
}
