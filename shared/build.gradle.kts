import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("showcase.primitive.kmp.android")
    id("showcase.primitive.kmp.skie")
    id("showcase.primitive.metro")
    id("showcase.primitive.spotless")
    id("showcase.primitive.detekt")
}

android {
    namespace = "io.github.yarn44.kmp.showcase.shared"
}

kotlin {
    // XCFramework bundles all iOS target binaries into a single distributable
    // artifact under shared/build/XCFrameworks/<buildType>/ShowcaseKit.xcframework.
    // The Swift Package (shared/Package.swift) consumes this via `binaryTarget`.
    val showcaseXcf = XCFramework("ShowcaseKit")

    // Allow restricting iOS targets for faster dev iteration. Passing
    // `-Papp.ios.shared.arch=arm64` drops iosX64 (Intel-Mac simulator), so
    // only iosArm64 (iPhone device) and iosSimulatorArm64 (Apple Silicon Mac
    // simulator) are built. Without the property, all three iOS targets are
    // built — typical for CI / release.
    val sharedArch = providers.gradleProperty("app.ios.shared.arch").orNull
    val includeIosX64 = sharedArch != "arm64"

    // iOS targets — produce a single umbrella framework.
    val iosTargets = listOfNotNull(
        if (includeIosX64) iosX64() else null,
        iosArm64(),
        iosSimulatorArm64(),
    )

    iosTargets.forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ShowcaseKit"
            isStatic = true
            showcaseXcf.add(this)

            // Re-export every module so that iOS can consume everything
            // via a single `import ShowcaseKit`.
            export(project(":core:foundation"))
            export(project(":core:data"))
            export(project(":core:ui"))
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
                api(project(":core:ui"))
                api(project(":feature:login"))
                api(project(":feature:home"))
                api(project(":feature:info"))
            }
        }

        val iosMain = create("iosMain") {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.kotlinxCoroutinesCore)
                // `IosAppGraph.provideDataStore` returns
                // `DataStore<Preferences>` so shared needs the symbol
                // directly. Mirrors the equivalent add in
                // `app/build.gradle.kts`.
                implementation(libs.datastorePreferencesCore)
            }
        }

        iosTargets.forEach { iosTarget ->
            getByName("${iosTarget.name}Main") {
                dependsOn(iosMain)
            }
        }
    }
}
