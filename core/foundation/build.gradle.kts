plugins {
    id("showcase.convention.kmp-module")
    id("showcase.primitive.kmp.compose")
    id("dev.zacsweers.metro")
    id("showcase.primitive.logging")
    id("showcase.primitive.unit-test")
}

android {
    namespace = "io.github.yarn44.kmp.showcase.core.foundation"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
        }
        androidMain.dependencies {
            implementation(libs.androidxCoreKtx)
            implementation(libs.navigationCompose)
            implementation(libs.composeAnimation)
        }
    }
}
