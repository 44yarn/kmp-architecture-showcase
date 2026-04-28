plugins {
    id("showcase.convention.kmp-module")
    id("showcase.convention.kmp-sqldelight")
    id("showcase.primitive.metro")
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
        }
        androidMain.dependencies {
            implementation(libs.sqldelightAndroidDriver)
        }
        iosMain {
            dependencies {
                // Koin dep removed during Stage 4 — iOS DI is now provided
                // entirely by Metro via `shared/src/iosMain/.../IosAppGraph.kt`.
                implementation(libs.sqldelightNativeDriver)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlinTest)
            implementation(libs.kotlinxCoroutinesTest)
        }
    }
}

sqldelight {
    databases {
        create("ShowcaseDatabase") {
            packageName.set("io.github.yarn44.kmp.showcase.core.data.db")
        }
    }
}
