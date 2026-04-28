plugins {
    id("showcase.convention.kmp-module")
    id("showcase.primitive.kmp.compose")
    id("showcase.primitive.metro")
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
            // KmpViewModel extends androidx.lifecycle.ViewModel (the KMP
            // version shipped in androidx.lifecycle:lifecycle-viewmodel 2.8+)
            // so that Compose's `viewModel { }` factory can scope instances
            // to a ViewModelStoreOwner on Android.
            implementation(libs.lifecycleViewModel)
        }
        androidMain.dependencies {
            implementation(libs.androidxCoreKtx)
            implementation(libs.navigationCompose)
            implementation(libs.composeAnimation)
        }
        // iosMain previously depended on Koin for `CoreKoinBridgeIos`.
        // After the Stage 4 Metro migration that bridge is gone, and
        // iosMain no longer has any iOS-specific dependencies of its
        // own — the Metro runtime is pulled in via
        // `showcase.primitive.metro`.
    }
}
