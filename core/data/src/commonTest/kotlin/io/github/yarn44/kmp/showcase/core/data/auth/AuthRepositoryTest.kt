package io.github.yarn44.kmp.showcase.core.data.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest

/**
 * Showcase test for [AuthRepository] demonstrating dispatcher substitution.
 *
 * The production [AuthRepository] wraps its body in
 * `withContext(ioDispatcher)` and contains a `delay(1500L)`. By passing
 * a `StandardTestDispatcher` linked to `runTest`'s `testScheduler`, the
 * delay is **virtualized** — the test advances `currentTime` by 1500 ms
 * without actually waiting.
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
