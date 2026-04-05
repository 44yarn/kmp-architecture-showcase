package showcase.util

import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

fun Project.configureKmpOrAndroid(
    commonMain: (DependencyHandler.(Project) -> Unit)? = null,
    androidMain: (DependencyHandler.(Project) -> Unit)? = null,
    iosMain: (DependencyHandler.(Project) -> Unit)? = null,
    androidOnly: (DependencyHandler.(Project) -> Unit)? = null,
    kmpKsp: (Project.() -> Unit)? = null,
) {
    pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                if (commonMain != null) {
                    getByName("commonMain") {
                        dependencies {
                            this@configureKmpOrAndroid.dependencies.commonMain(this@configureKmpOrAndroid)
                        }
                    }
                }
                if (androidMain != null) {
                    findByName("androidMain")?.dependencies {
                        this@configureKmpOrAndroid.dependencies.androidMain(this@configureKmpOrAndroid)
                    }
                }
                if (iosMain != null) {
                    findByName("iosMain")?.dependencies {
                        this@configureKmpOrAndroid.dependencies.iosMain(this@configureKmpOrAndroid)
                    }
                }
            }
        }
        kmpKsp?.invoke(this@configureKmpOrAndroid)
    }

    if (androidOnly != null) {
        pluginManager.withPlugin("com.android.library") {
            if (!pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                dependencies { androidOnly(this@configureKmpOrAndroid) }
            }
        }
        pluginManager.withPlugin("com.android.application") {
            if (!pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
                dependencies { androidOnly(this@configureKmpOrAndroid) }
            }
        }
    }
}
