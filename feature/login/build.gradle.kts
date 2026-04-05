plugins {
    id("showcase.convention.kmp-feature")
    alias(libs.plugins.kmpNativeCoroutines)
}

android {
    namespace = "io.github.mitsuharu.showcase.feature.login"
}
