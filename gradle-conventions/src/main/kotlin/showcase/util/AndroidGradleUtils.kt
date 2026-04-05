package showcase.util

import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

fun Project.androidApplication(block: BaseAppModuleExtension.() -> Unit) {
    extensions.configure(block)
}

fun Project.androidLibrary(block: LibraryExtension.() -> Unit) {
    extensions.configure(block)
}

fun Project.android(block: BaseExtension.() -> Unit) {
    extensions.configure(block)
}

fun Project.androidComponents(block: LibraryAndroidComponentsExtension.() -> Unit) {
    extensions.configure(block)
}

fun Project.kotlinAndroidOptions(block: KotlinAndroidProjectExtension.() -> Unit) {
    extensions.configure(block)
}
