package showcase.primitive

import showcase.config.IosConfig
import showcase.util.libs
import showcase.util.library
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

fun iosFrameworkBaseName(): String = project.path.removePrefix(":").replace(":", "_").replace("-", "_")

// KMP iOS target 設定
extensions.configure<KotlinMultiplatformExtension> {
    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    )

    // フレームワーク設定
    iosTargets.forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = iosFrameworkBaseName()
            isStatic = true

            freeCompilerArgs += listOf(
                "-Xbinary=bundleId=${IosConfig.BUNDLE_ID_PREFIX}.${project.name}",
            )

            linkerOpts.addAll(
                listOf(
                    "-framework", "Foundation",
                    "-framework", "UIKit",
                ),
            )
        }
    }

    // iOS ソースセット設定
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
