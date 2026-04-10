package showcase.primitive

import showcase.util.libs
import showcase.util.library
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

// KMP iOS target configuration.
// Framework generation is centralized in the `shared` module; individual
// modules only declare their iOS targets and source sets here.
extensions.configure<KotlinMultiplatformExtension> {
    iosX64()
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

        listOf("iosX64", "iosArm64", "iosSimulatorArm64").forEach { targetName ->
            getByName("${targetName}Main") {
                dependsOn(iosMain)
            }
            getByName("${targetName}Test") {
                dependsOn(iosTest)
            }
        }
    }
}
