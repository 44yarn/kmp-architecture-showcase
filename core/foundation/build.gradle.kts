plugins {
    id("showcase.convention.kmp-module")
    id("showcase.primitive.kmp.compose")
    id("showcase.primitive.hilt")
    id("showcase.primitive.logging")
    id("showcase.primitive.unit-test")
}

android {
    namespace = "io.github.mitsuharu.showcase.core.foundation"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            api(libs.kmpObservableViewModelCore)
        }
        androidMain.dependencies {
            implementation(libs.androidxCoreKtx)
            implementation(libs.navigationCompose)
            implementation(libs.composeAnimation)
        }
        iosMain {
            dependencies {
                implementation(libs.koinCore)
            }
        }
    }
}
