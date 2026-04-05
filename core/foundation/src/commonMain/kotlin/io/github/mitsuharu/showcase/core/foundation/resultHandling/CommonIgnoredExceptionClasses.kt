package io.github.mitsuharu.showcase.core.foundation.resultHandling

import kotlin.coroutines.cancellation.CancellationException
import kotlin.reflect.KClass

val commonIgnoredExceptionClasses: List<KClass<out Throwable>> = listOf(
    CancellationException::class,
)
