package showcase.primitive

import showcase.util.libs
import showcase.util.library
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

// KMP iOS target configuration.
// NOTE: iosX64 (Intel-Mac simulator) is dropped because Compose Multiplatform
// 1.11.0+ no longer ships Apple x86_64 artifacts (KT-81596).
// Framework generation is centralized in the `shared` module; individual
// modules only declare their iOS targets and source sets here.
extensions.configure<KotlinMultiplatformExtension> {
    iosArm64()
    iosSimulatorArm64()

    // iOS source set configuration.
    sourceSets.apply {
        val iosMain = create("iosMain") {
            dependsOn(getByName("commonMain"))
            dependencies {
                implementation(libs.library("kotlinxCoroutinesCore"))
            }
        }

        val iosTest = create("iosTest") {
            dependsOn(getByName("commonTest"))
        }

        listOf("iosArm64", "iosSimulatorArm64").forEach { targetName ->
            getByName("${targetName}Main") {
                dependsOn(iosMain)
            }
            getByName("${targetName}Test") {
                dependsOn(iosTest)
            }
        }
    }
}
