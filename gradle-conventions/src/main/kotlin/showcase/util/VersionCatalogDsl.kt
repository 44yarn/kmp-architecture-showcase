package showcase.util

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.gradle.plugin.use.PluginDependency

val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun VersionCatalog.version(name: String): String =
    findVersion(name).get().requiredVersion

fun VersionCatalog.library(name: String): Provider<MinimalExternalModuleDependency> =
    findLibrary(name).get()

fun VersionCatalog.plugin(name: String): Provider<PluginDependency> =
    findPlugin(name).get()
