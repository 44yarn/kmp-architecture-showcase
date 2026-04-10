package io.github.yarn44.kmp.showcase.core.data.auth

import io.github.yarn44.kmp.showcase.core.foundation.resultHandling.logOnFailure
import io.github.yarn44.kmp.showcase.core.foundation.resultHandling.runCatchingCancellable
import kotlinx.coroutines.delay

class AuthRepository {
    suspend fun login(email: String, password: String): Result<String> =
        runCatchingCancellable {
            delay(1500L)
            if (password == "error") {
                error("Invalid credentials")
            }
            email.substringBefore("@").replaceFirstChar { it.uppercase() }
        }.logOnFailure()
}
