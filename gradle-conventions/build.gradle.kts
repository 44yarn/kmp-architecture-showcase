import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.androidGradlePlugin)
    implementation(libs.kotlinGradlePlugin)
    implementation(libs.composeMultiplatformPlugin)
    implementation(libs.detektGradlePlugin)
    implementation(libs.spotlessGradlePlugin)
    implementation(libs.sqldelightGradlePlugin)

    val skieVersion = libs.versions.skie.get()
    implementation("co.touchlab.skie:co.touchlab.skie.gradle.plugin:$skieVersion")

    // Gradle Plugin Marker Artifacts for precompiled script plugins
    val kotlinVersion = libs.versions.kotlin.get()
    implementation("org.jetbrains.kotlin.android:org.jetbrains.kotlin.android.gradle.plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin.multiplatform:org.jetbrains.kotlin.multiplatform.gradle.plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin.plugin.serialization:org.jetbrains.kotlin.plugin.serialization.gradle.plugin:$kotlinVersion")
    implementation("org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:$kotlinVersion")

    val agpVersion = libs.versions.agp.get()
    implementation("com.android.application:com.android.application.gradle.plugin:$agpVersion")
    implementation("com.android.library:com.android.library.gradle.plugin:$agpVersion")

    val detektVersion = libs.versions.detekt.get()
    implementation("io.gitlab.arturbosch.detekt:io.gitlab.arturbosch.detekt.gradle.plugin:$detektVersion")

    val composeMultiplatformVersion = libs.versions.composeMultiplatform.get()
    implementation("org.jetbrains.compose:org.jetbrains.compose.gradle.plugin:$composeMultiplatformVersion")

    val sqldelightVersion = libs.versions.sqldelight.get()
    implementation("app.cash.sqldelight:app.cash.sqldelight.gradle.plugin:$sqldelightVersion")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        languageVersion.set(KotlinVersion.KOTLIN_2_2)
        freeCompilerArgs.add("-Xuse-fir-lt=false")
    }
}
