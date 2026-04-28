plugins {
    id("showcase.convention.kmp-feature")
    id("showcase.primitive.kotlin-inject")
}

android {
    namespace = "io.github.yarn44.kmp.showcase.feature.home"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinInjectRuntime)
            implementation(libs.kotlinInjectAnvilRuntime)
            implementation(libs.kotlinInjectAnvilRuntimeOptional)
        }
    }
}

dependencies {
    add("kspAndroid", libs.kotlinInjectCompiler)
    add("kspAndroid", libs.kotlinInjectAnvilCompiler)
    listOf("kspIosX64", "kspIosArm64", "kspIosSimulatorArm64").forEach { config ->
        add(config, libs.kotlinInjectCompiler)
        add(config, libs.kotlinInjectAnvilCompiler)
    }
}
