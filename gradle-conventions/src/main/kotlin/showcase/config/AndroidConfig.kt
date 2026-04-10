package showcase.config

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object AndroidConfig {
    const val compileSdk = 36
    const val minSdk = 31
    const val targetSdk = 36
    const val versionCode = 1
    const val versionName = "1.0.0"
    const val applicationId = "io.github.yarn44.kmp.showcase"
    val javaVersion = JavaVersion.VERSION_21
    val jvmTarget = JvmTarget.JVM_21
}
