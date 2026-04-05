package showcase.primitive

import showcase.util.libs
import showcase.util.library

plugins {
    id("org.jetbrains.kotlin.plugin.compose")
}

pluginManager.withPlugin("com.android.library") {
    extensions.findByType(com.android.build.gradle.LibraryExtension::class.java)?.apply {
        buildFeatures {
            compose = true
        }
    }
}

pluginManager.withPlugin("com.android.application") {
    extensions.findByType(com.android.build.gradle.internal.dsl.BaseAppModuleExtension::class.java)?.apply {
        buildFeatures {
            compose = true
        }
    }
}

dependencies {
    add("implementation", platform(libs.library("composeBom")))
    add("androidTestImplementation", platform(libs.library("composeBom")))

    add("implementation", libs.library("composeAnimation"))
    add("implementation", libs.library("composeFoundation"))
    add("implementation", libs.library("composeMaterial3"))
    add("implementation", libs.library("composeUi"))
    add("implementation", libs.library("composeUiGraphics"))
    add("implementation", libs.library("composeUiToolingPreview"))
    add("implementation", libs.library("activityCompose"))
    add("implementation", libs.library("lifecycleRuntimeCompose"))
    add("implementation", libs.library("lifecycleViewmodelCompose"))
    add("implementation", libs.library("navigationCompose"))

    add("debugImplementation", libs.library("composeUiTooling"))
    add("debugImplementation", libs.library("composeUiTestManifest"))
}
