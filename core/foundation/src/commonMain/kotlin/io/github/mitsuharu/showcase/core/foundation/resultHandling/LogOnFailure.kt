package io.github.mitsuharu.showcase.core.foundation.resultHandling

import io.github.mitsuharu.showcase.core.foundation.logging.Trunk
import kotlin.reflect.KClass

fun <T> Result<T>.logOnFailure(
    ignoredExceptionClasses: List<KClass<out Throwable>> = commonIgnoredExceptionClasses,
): Result<T> = onFailure { exception ->
    if (ignoredExceptionClasses.none { it.isInstance(exception) }) {
        Trunk.e(exception, "Operation failed")
    }
}
