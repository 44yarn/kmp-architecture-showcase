plugins {
    id("showcase.convention.kmp-module")
    id("showcase.primitive.kmp.compose")
    id("showcase.primitive.kotlin-inject")
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
            implementation(libs.kotlinInjectRuntime)
            implementation(libs.kotlinInjectAnvilRuntime)
            implementation(libs.kotlinInjectAnvilRuntimeOptional)
        }
        androidMain.dependencies {
            implementation(libs.androidxCoreKtx)
            implementation(libs.navigationCompose)
            implementation(libs.composeAnimation)
        }
        iosMain {
        }
    }
}

dependencies {
    add("kspAndroid", libs.kotlinInjectCompiler)
    add("kspAndroid", libs.kotlinInjectAnvilCompiler)
    listOf("kspIosArm64", "kspIosSimulatorArm64").forEach { config ->
        add(config, libs.kotlinInjectCompiler)
        add(config, libs.kotlinInjectAnvilCompiler)
    }
}
