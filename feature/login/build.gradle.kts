plugins {
    id("showcase.convention.kmp-feature")
}

android {
    namespace = "io.github.mitsuharu.showcase.feature.login"
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.composeMaterialIconsExtended)
        }
    }
}
