package io.github.yarn44.kmp.showcase.core.data.auth

import io.github.yarn44.kmp.showcase.core.data.testing.TestDispatcherProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

/**
 * Showcase test for [AuthRepository] demonstrating the [DispatcherProvider]
 * substitution pattern.
 *
 * The production [AuthRepository] wraps its body in
 * `withContext(dispatchers.io)` and contains a `delay(1500L)`. By injecting
 * a [TestDispatcherProvider] backed by a `StandardTestDispatcher` linked to
 * `runTest`'s `testScheduler`, the delay is **virtualized** — the test
 * advances `currentTime` by 1500 ms without actually waiting.
 *
 * Without the [DispatcherProvider] indirection, the only way to test this
 * Repository would be to wait 1.5 real seconds per test case, or to use
 * brittle thread-manipulation hacks.
 */
class AuthRepositoryTest {

    @Test
    fun loginReturnsCapitalizedNameFromEmailWhenPasswordIsValid() = runTest {
        val dispatchers = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
        val repository = AuthRepository(dispatchers)

        val result = repository.login("alice@example.com", "anything")

        assertTrue(result.isSuccess, "Expected success but got $result")
        assertEquals("Alice", result.getOrNull())
    }

    @Test
    fun loginReturnsAuthExceptionWhenPasswordIsErrorSentinel() = runTest {
        val dispatchers = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
        val repository = AuthRepository(dispatchers)

        val result = repository.login("alice@example.com", "error")

        assertTrue(result.isFailure, "Expected failure but got $result")
        assertTrue(
            result.exceptionOrNull() is AuthException,
            "Expected AuthException but got ${result.exceptionOrNull()}",
        )
    }

    /**
     * The killer demo for [DispatcherProvider]: this test asserts that the
     * production `delay(1500L)` advances **virtual** time by exactly 1500 ms.
     * Without dispatcher substitution this assertion would be impossible —
     * the test would either take 1.5 real seconds or fail.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loginVirtualizesProductionDelayUnderTestDispatcher() = runTest {
        val dispatchers = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
        val repository = AuthRepository(dispatchers)

        val virtualTimeBefore = testScheduler.currentTime
        repository.login("bob@example.com", "password")
        val virtualTimeAfter = testScheduler.currentTime

        assertEquals(1500L, virtualTimeAfter - virtualTimeBefore)
    }
}
