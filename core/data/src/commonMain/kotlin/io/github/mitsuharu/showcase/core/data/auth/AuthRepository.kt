package io.github.mitsuharu.showcase.core.data.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
}
