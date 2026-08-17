plugins {
    id("showcase.convention.kmp-feature")
    id("dev.zacsweers.metro")
}

android {
    namespace = "io.github.yarn44.kmp.showcase.feature.info"
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.composeMaterialIconsExtended)
        }
    }
}
