plugins {
    id("showcase.convention.kmp-feature")
    id("showcase.primitive.kotlin-inject")
}

android {
    namespace = "io.github.yarn44.kmp.showcase.feature.info"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinInjectRuntime)
            implementation(libs.kotlinInjectAnvilRuntime)
            implementation(libs.kotlinInjectAnvilRuntimeOptional)
        }
        androidMain.dependencies {
            implementation(libs.composeMaterialIconsExtended)
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
