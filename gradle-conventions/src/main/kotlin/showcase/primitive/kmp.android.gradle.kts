package showcase.primitive

import com.android.build.gradle.LibraryExtension
import showcase.config.AndroidConfig
import showcase.util.libs
import showcase.util.library
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

// KMP Android target 設定
extensions.configure<KotlinMultiplatformExtension> {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.addAll(
                        listOf(
                            "-opt-in=kotlin.RequiresOptIn",
                            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                        ),
                    )
                }
            }
        }
    }

    sourceSets.apply {
        getByName("androidMain") {
            dependencies {
                implementation(libs.library("androidxCoreKtx"))
                implementation(libs.library("lifecycleRuntimeKtx"))
            }
        }
    }
}

// Android Library 設定
extensions.configure<LibraryExtension> {
    namespace = "io.github.mitsuharu.showcase.${project.name.replace("-", ".")}"
    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        minSdk = AndroidConfig.minSdk
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = AndroidConfig.javaVersion
        targetCompatibility = AndroidConfig.javaVersion
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(AndroidConfig.jvmTarget)
    }
}
