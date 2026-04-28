plugins {
    id("showcase.convention.kmp-module")
    id("showcase.convention.kmp-sqldelight")
    id("showcase.primitive.kotlin-inject")
    id("showcase.primitive.unit-test")
}

android {
    namespace = "io.github.yarn44.kmp.showcase.core.data"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:foundation"))
            implementation(libs.sqldelightRuntime)
            implementation(libs.sqldelightCoroutines)
            implementation(libs.kotlinxSerializationJson)
            api(libs.datastorePreferencesCore)
            implementation(libs.kotlinInjectRuntime)
            implementation(libs.kotlinInjectAnvilRuntime)
            implementation(libs.kotlinInjectAnvilRuntimeOptional)
        }
        androidMain.dependencies {
            implementation(libs.sqldelightAndroidDriver)
        }
        iosMain {
            dependencies {
                implementation(libs.sqldelightNativeDriver)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlinTest)
            implementation(libs.kotlinxCoroutinesTest)
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

sqldelight {
    databases {
        create("ShowcaseDatabase") {
            packageName.set("io.github.yarn44.kmp.showcase.core.data.db")
        }
    }
}
