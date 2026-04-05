plugins {
    id("showcase.convention.kmp-module")
    id("showcase.convention.kmp-sqldelight")
    id("showcase.primitive.preferences")
    id("showcase.primitive.hilt")
    id("showcase.primitive.unit-test")
}

android {
    namespace = "io.github.mitsuharu.showcase.core.data"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:foundation"))
            implementation(libs.sqldelightRuntime)
            implementation(libs.sqldelightCoroutines)
            implementation(libs.kotlinxSerializationJson)
        }
        androidMain.dependencies {
            implementation(libs.sqldelightAndroidDriver)
            implementation(libs.datastorePreferences)
        }
        iosMain {
            dependencies {
                implementation(libs.koinCore)
                implementation(libs.sqldelightNativeDriver)
            }
        }
    }
}

sqldelight {
    databases {
        create("ShowcaseDatabase") {
            packageName.set("io.github.mitsuharu.showcase.core.data.db")
        }
    }
}
