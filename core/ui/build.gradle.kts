plugins {
    id("showcase.convention.kmp-module")
    id("showcase.primitive.kmp.compose")
    id("showcase.primitive.unit-test")
}

android {
    namespace = "io.github.yarn44.kmp.showcase.core.ui"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:foundation"))
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            // For StringResource type used by AdaptiveString.
            // Actual resource files live in feature modules, not here.
            implementation(compose.components.resources)
        }
        androidMain.dependencies {
            implementation(libs.composeUiTooling)
        }
    }
}
