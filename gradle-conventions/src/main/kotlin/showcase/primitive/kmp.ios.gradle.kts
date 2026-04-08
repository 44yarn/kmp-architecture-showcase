package showcase.primitive

import showcase.util.libs
import showcase.util.library
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

// KMP iOS target 設定
// framework 生成は shared に一本化。個別モジュールは iOS ターゲットとソースセットのみ定義する。
extensions.configure<KotlinMultiplatformExtension> {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

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
