package showcase.convention

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import showcase.config.AndroidConfig
import showcase.util.addFeatureModuleDependencies

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("showcase.primitive.android-config")
    id("showcase.primitive.spotless")
    id("showcase.primitive.detekt")
}

pluginManager.withPlugin("com.android.application") {
    extensions.getByType(BaseAppModuleExtension::class.java).apply {
        namespace = AndroidConfig.applicationId
        compileSdk = AndroidConfig.compileSdk

        defaultConfig {
            applicationId = AndroidConfig.applicationId
            versionCode = AndroidConfig.versionCode
            versionName = AndroidConfig.versionName
        }

        buildTypes {
            debug {
                isDebuggable = true
                isMinifyEnabled = false
            }
            release {
                isMinifyEnabled = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                )
            }
        }

        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
                excludes += "META-INF/versions/9/previous-compilation-data.bin"
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(AndroidConfig.jvmTarget)
    }
}

addFeatureModuleDependencies()
