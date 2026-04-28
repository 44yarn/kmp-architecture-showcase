package showcase.primitive

import showcase.util.configureKmpOrAndroid
import showcase.util.libs
import showcase.util.library

// Metro is a Kotlin compiler plugin (not a KSP processor). It works
// uniformly across all KMP targets without extra ksp wiring. Runtime
// annotations (`@Inject`, `@Provides`, `@DependencyGraph`, `@Qualifier`,
// `@ContributesTo`, `@ContributesIntoMap`, etc.) live in
// `dev.zacsweers.metro:runtime`.
//
// Note on apply strategy:
// Metro 0.13.2 accesses the `kotlin` extension at plugin apply time, so
// declaring it in a precompiled script plugin's `plugins { }` block breaks
// `generatePrecompiledScriptPluginAccessors` (which evaluates the plugins
// block against a minimal test project that has no Kotlin extension).
// Instead, defer application until after the Kotlin plugin is on the
// consumer project via `pluginManager.withPlugin(...)`. This mirrors how
// mature Gradle plugins like SKIE handle the same constraint internally.
pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
    pluginManager.apply("dev.zacsweers.metro")
}
pluginManager.withPlugin("org.jetbrains.kotlin.android") {
    if (!pluginManager.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
        pluginManager.apply("dev.zacsweers.metro")
    }
}

// Metro runtime provides the core annotations (`@Inject`, `@Provides`,
// `@DependencyGraph`, `@Qualifier`, `@SingleIn`, etc.).
configureKmpOrAndroid(
    commonMain = { project ->
        add("implementation", project.libs.library("metroRuntime"))
    },
    androidOnly = { project ->
        add("implementation", project.libs.library("metroRuntime"))
    },
)
