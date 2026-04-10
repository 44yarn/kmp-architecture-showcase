package io.github.yarn44.kmp.showcase.core.foundation.resultHandling

import io.github.yarn44.kmp.showcase.core.foundation.logging.Trunk
import kotlin.reflect.KClass

fun <T> Result<T>.logOnFailure(
    ignoredExceptionClasses: List<KClass<out Throwable>> = commonIgnoredExceptionClasses,
): Result<T> = onFailure { exception ->
    if (ignoredExceptionClasses.none { it.isInstance(exception) }) {
        Trunk.e(exception, "Operation failed")
    }
}
