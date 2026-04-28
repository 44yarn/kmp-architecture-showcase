package io.github.yarn44.kmp.showcase.core.foundation.coroutines

import dev.zacsweers.metro.Qualifier

/**
 * Metro [Qualifier] marker identifying the IO-bound [kotlinx.coroutines.CoroutineDispatcher]
 * — used to route Repository `withContext(...)` blocks off the main thread.
 *
 * Living in `commonMain` is the whole reason we migrated away from Hilt's
 * `javax.inject.Qualifier` (which is JVM-only): this annotation is visible
 * to both Android and iOS source sets, so platform-specific DI glue is no
 * longer required for dispatcher injection.
 */
@Qualifier
annotation class IoDispatcher
