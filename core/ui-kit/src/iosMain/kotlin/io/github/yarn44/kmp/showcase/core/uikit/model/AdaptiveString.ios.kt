package io.github.yarn44.kmp.showcase.core.uikit.model

import io.github.yarn44.kmp.showcase.core.foundation.logging.Trunk
import org.jetbrains.compose.resources.getString

/**
 * Resolves an [AdaptiveString] into a plain [String] from a non-Composable
 * context — primarily for SwiftUI consumers that cannot call the
 * `@Composable val value` accessor.
 *
 * The function is `suspend` because [getString] is suspend; SKIE bridges
 * it to an `async` Swift function, so SwiftUI can call it from a
 * `.task { ... }` block and await the result.
 *
 * Any error thrown by the underlying Compose Resources runtime is logged
 * and swallowed here. Returning an empty string is preferred over letting
 * the exception propagate to the coroutine's final exception handler,
 * which would crash the process — a poor trade-off for a UI text lookup.
 */
@Suppress("detekt.SpreadOperator", "detekt.TooGenericExceptionCaught")
suspend fun AdaptiveString.resolve(): String {
    text?.let { return it }
    val res = resource ?: return ""
    val args = formatArgs
    return try {
        if (args != null) getString(res, *args) else getString(res)
    } catch (t: Throwable) {
        Trunk.e(t, "AdaptiveString.resolve failed for $res")
        ""
    }
}
