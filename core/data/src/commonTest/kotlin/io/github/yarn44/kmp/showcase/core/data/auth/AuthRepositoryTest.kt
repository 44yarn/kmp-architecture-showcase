package io.github.yarn44.kmp.showcase.core.data.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

/**
 * Showcase test for [AuthRepository] demonstrating test-dispatcher
 * substitution via Metro's `@IoDispatcher` constructor injection.
 *
 * The production [AuthRepository] wraps its body in
 * `withContext(ioDispatcher)` and contains a `delay(1500L)`. By passing a
 * `StandardTestDispatcher` linked to `runTest`'s `testScheduler` as the
 * `CoroutineDispatcher` argument, the delay is **virtualized** — the test
 * advances `currentTime` by 1500 ms without actually waiting.
 *
 * For unit tests we construct [AuthRepository] directly rather than going
 * through a Metro `@DependencyGraph`. Tests that need a real Metro graph
 * with swapped-in bindings can instead declare a
 * `@ContributesTo(AppScope::class, replaces = [CoroutineDependenciesProviders::class])`
 * test interface, following the `TestCoroutineDependenciesProviders`
 * pattern from DroidKaigi conference-app-2025.
 */
class AuthRepositoryTest {

    @Test
    fun loginReturnsCapitalizedNameFromEmailWhenPasswordIsValid() = runTest {
        val repository = AuthRepository(StandardTestDispatcher(testScheduler))

        val result = repository.login("alice@example.com", "anything")

        assertTrue(result.isSuccess, "Expected success but got $result")
        assertEquals("Alice", result.getOrNull())
    }

    @Test
    fun loginReturnsAuthExceptionWhenPasswordIsErrorSentinel() = runTest {
        val repository = AuthRepository(StandardTestDispatcher(testScheduler))

        val result = repository.login("alice@example.com", "error")

        assertTrue(result.isFailure, "Expected failure but got $result")
        assertTrue(
            result.exceptionOrNull() is AuthException,
            "Expected AuthException but got ${result.exceptionOrNull()}",
        )
    }

    /**
     * The killer demo for test dispatcher substitution: this test asserts
     * that the production `delay(1500L)` advances **virtual** time by
     * exactly 1500 ms. Without dispatcher substitution this assertion would
     * be impossible — the test would either take 1.5 real seconds or fail.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loginVirtualizesProductionDelayUnderTestDispatcher() = runTest {
        val repository = AuthRepository(StandardTestDispatcher(testScheduler))

        val virtualTimeBefore = testScheduler.currentTime
        repository.login("bob@example.com", "password")
        val virtualTimeAfter = testScheduler.currentTime

        assertEquals(1500L, virtualTimeAfter - virtualTimeBefore)
    }
}
