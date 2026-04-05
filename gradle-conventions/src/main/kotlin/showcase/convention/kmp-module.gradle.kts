package showcase.convention

import showcase.util.libs
import showcase.util.library

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("showcase.primitive.kmp.android")
    id("showcase.primitive.kmp.ios")
    id("showcase.primitive.spotless")
    id("showcase.primitive.detekt")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.library("kotlinxCoroutinesCore"))
            }
        }

        commonTest {
            dependencies {
                implementation(libs.library("kotlinTest"))
                implementation(libs.library("kotestAssertionsCore"))
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
}
