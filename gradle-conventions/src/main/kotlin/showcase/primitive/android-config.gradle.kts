package showcase.primitive

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import showcase.config.AndroidConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Android の基本設定を行う primitive Plugin。
 * compileSdk, minSdk, targetSdk, Java/Kotlin JVM 設定。
 */
fun configureAndroid(extension: BaseExtension) {
    extension.apply {
        compileSdkVersion(AndroidConfig.compileSdk)

        defaultConfig {
            minSdk = AndroidConfig.minSdk
            if (extension is AppExtension) {
                targetSdkVersion(AndroidConfig.targetSdk)
            }
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        compileOptions {
            sourceCompatibility = AndroidConfig.javaVersion
            targetCompatibility = AndroidConfig.javaVersion
        }
    }
}

pluginManager.withPlugin("com.android.application") {
    configureAndroid(extensions.getByType(AppExtension::class.java))
}
pluginManager.withPlugin("com.android.library") {
    configureAndroid(extensions.getByType(LibraryExtension::class.java))
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(AndroidConfig.jvmTarget)
        freeCompilerArgs.add("-Xannotation-default-target=param-property")
    }
}
