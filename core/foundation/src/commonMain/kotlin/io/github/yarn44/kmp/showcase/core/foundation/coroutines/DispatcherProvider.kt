package io.github.yarn44.kmp.showcase.core.foundation.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

/**
 * Provides [CoroutineDispatcher]s to the rest of the app. Repositories and
 * other IO-bound code should inject this instead of referencing
 * `Dispatchers.IO` / `.Default` / `.Main` directly, so tests can substitute
 * a [DispatcherProvider] backed by `kotlinx-coroutines-test`'s
 * `TestDispatcher` (e.g. `StandardTestDispatcher`).
 *
 * ## Why an interface (not just direct `Dispatchers.X` calls)?
 *
 * The concrete `Dispatchers.IO` / `.Default` / `.Main` objects are Kotlin
 * runtime singletons — they cannot be swapped from the outside without an
 * indirection. Introducing a thin interface gives tests a single point to
 * substitute, and keeps Repository code free of direct runtime references.
 *
 * ## Why not `@IoDispatcher` qualifier annotations?
 *
 * Hilt's `@Qualifier` meta-annotation comes from `javax.inject` which is
 * JVM-only and cannot be referenced from `commonMain`. Koin uses a
 * different (string/typed) qualifier idiom. A plain interface is the only
 * approach that works uniformly under Hilt (Android) and Koin (iOS) without
 * duplicating qualifier definitions per DI framework.
 *
 * The default production binding is [DefaultDispatcherProvider]; wire it up
 * in each platform's DI module (`RepositoryModule` for Hilt,
 * `coreDataKoinModule` for Koin).
 */
interface DispatcherProvider {
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val main: CoroutineDispatcher
}

/**
 * Production [DispatcherProvider] backed by the `kotlinx.coroutines`
 * built-in [Dispatchers].
 */
class DefaultDispatcherProvider : DispatcherProvider {
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
    override val main: CoroutineDispatcher = Dispatchers.Main
}
