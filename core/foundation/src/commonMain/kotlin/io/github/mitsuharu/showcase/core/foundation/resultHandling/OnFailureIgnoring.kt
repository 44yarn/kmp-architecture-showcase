package io.github.mitsuharu.showcase.core.foundation.resultHandling

import kotlin.reflect.KClass

inline fun <T> Result<T>.onFailureIgnoring(
    ignoredExceptionClasses: List<KClass<out Throwable>> = commonIgnoredExceptionClasses,
    action: (Throwable) -> Unit,
): Result<T> = onFailure { exception ->
    if (ignoredExceptionClasses.none { it.isInstance(exception) }) {
        action(exception)
    }
}
