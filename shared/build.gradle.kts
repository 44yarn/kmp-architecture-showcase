plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("showcase.primitive.kmp.android")
    id("showcase.primitive.spotless")
    id("showcase.primitive.detekt")
}

android {
    namespace = "io.github.mitsuharu.showcase.shared"
}

kotlin {
    // iOS ターゲット — 単一の umbrella フレームワークを生成
    val iosTargets = listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    )

    iosTargets.forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ShowcaseKit"
            isStatic = true

            // 全モジュールを re-export して iOS から単一 import で利用可能にする
            export(project(":core:foundation"))
            export(project(":core:data"))
            export(project(":core:ui-kit"))
            export(project(":feature:login"))
            export(project(":feature:home"))
            export(project(":feature:info"))

            linkerOpts.addAll(
                listOf(
                    "-framework", "Foundation",
                    "-framework", "UIKit",
                ),
            )
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":core:foundation"))
                api(project(":core:data"))
                api(project(":core:ui-kit"))
                api(project(":feature:login"))
                api(project(":feature:home"))
                api(project(":feature:info"))
            }
        }

        val iosMain = create("iosMain") {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.koinCore)
                implementation(libs.kotlinxCoroutinesCore)
            }
        }

        listOf("iosX64", "iosArm64", "iosSimulatorArm64").forEach { targetName ->
            getByName("${targetName}Main") {
                dependsOn(iosMain)
            }
        }
    }
}
