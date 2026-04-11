package io.github.yarn44.kmp.showcase.core.data.testing

import io.github.yarn44.kmp.showcase.core.foundation.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher

/**
 * Test [DispatcherProvider] that routes all three dispatcher slots to a
 * single shared [TestDispatcher]. This lets `runTest` virtualize `delay`
 * calls inside Repository code that uses `withContext(dispatchers.io)`,
 * because the dispatcher and the test scheduler share a `TestCoroutineScheduler`.
 *
 * Typical usage from a test:
 *
 * ```kotlin
 * @Test
 * fun example() = runTest {
 *     val dispatchers = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
 *     val repository = AuthRepository(dispatchers)
 *     // delay(1500L) inside the repository advances virtual time, not real time
 *     repository.login("alice@example.com", "password")
 * }
 * ```
 */
class TestDispatcherProvider(private val testDispatcher: TestDispatcher,) : DispatcherProvider {
    override val io: CoroutineDispatcher = testDispatcher
    override val default: CoroutineDispatcher = testDispatcher
    override val main: CoroutineDispatcher = testDispatcher
}
