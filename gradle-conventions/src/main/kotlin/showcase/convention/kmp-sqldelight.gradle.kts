package showcase.convention

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("app.cash.sqldelight")
}

// Link SQLite on Apple targets automatically (required by Native driver)
(extensions.findByName("kotlin") as? KotlinMultiplatformExtension)?.let { kmp ->
    kmp.targets.withType(KotlinNativeTarget::class.java).configureEach {
        if (konanTarget.family.isAppleFamily) {
            binaries.all { linkerOpts("-lsqlite3") }
        }
    }
}
