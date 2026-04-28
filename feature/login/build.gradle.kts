plugins {
    id("showcase.convention.kmp-feature")
    id("showcase.primitive.kotlin-inject")
}

android {
    namespace = "io.github.yarn44.kmp.showcase.feature.login"
}

// Compose Multiplatform Resources: generate a typed `Res` class for the
// string resources declared under src/commonMain/composeResources/.
compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.yarn44.kmp.showcase.feature.login.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.components.resources)
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
    listOf("kspIosX64", "kspIosArm64", "kspIosSimulatorArm64").forEach { config ->
        add(config, libs.kotlinInjectCompiler)
        add(config, libs.kotlinInjectAnvilCompiler)
    }
}
