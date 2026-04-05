plugins {
    id("showcase.convention.kmp-module")
    id("showcase.primitive.kmp.compose")
    id("showcase.primitive.hilt")
    id("showcase.primitive.unit-test")
    alias(libs.plugins.kmpNativeCoroutines)
}

android {
    namespace = "io.github.mitsuharu.showcase.core.uikit"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:foundation"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(libs.nativecoroutinesCore)
        }
        androidMain.dependencies {
            implementation(libs.composeUiTooling)
        }
    }
}
