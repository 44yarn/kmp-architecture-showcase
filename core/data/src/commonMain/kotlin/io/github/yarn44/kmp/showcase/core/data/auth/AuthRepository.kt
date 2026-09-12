package io.github.yarn44.kmp.showcase.core.data.auth

import dev.zacsweers.metro.Inject
import io.github.yarn44.kmp.showcase.core.foundation.coroutines.IoDispatcher
import io.github.yarn44.kmp.showcase.core.foundation.resultHandling.logOnFailure
import io.github.yarn44.kmp.showcase.core.foundation.resultHandling.runCatchingCancellable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Inject
class AuthRepository(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun login(email: String, password: String): Result<String> =
        withContext(ioDispatcher) {
            runCatchingCancellable {
                delay(1500L)
                if (password == "error") {
                    throw AuthException("Authentication failed: invalid credentials")
                }
                email.substringBefore("@").replaceFirstChar { it.uppercase() }
            }.logOnFailure()
        }
}

/**
 * Sample-only exception for showcasing how the UI layer can map error types
 * to user-facing text (see `LoginViewModel.showLoginErrorDialog`).
 *
 * IMPORTANT — design note for real projects:
 *
 * This showcase intentionally defines a single dedicated exception class so
 * that a reader can see the "type-based error -> UI text mapping" pattern.
 * In real projects you should **avoid proliferating custom exception types**:
 * typically one reportable exception (for crash reporting) is enough, and
 * rich failure information should be modeled as data (e.g. sealed result
 * types) rather than as a deep exception hierarchy.
 *
 * @param message Diagnostic message for logging, not for UI display.
 *     UI-facing text must be chosen by the UI layer (see `AdaptiveString`).
 */
class AuthException(message: String) : RuntimeException(message)
