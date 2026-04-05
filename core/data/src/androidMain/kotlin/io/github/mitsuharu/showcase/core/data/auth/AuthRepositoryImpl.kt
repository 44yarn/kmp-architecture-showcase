package io.github.mitsuharu.showcase.core.data.auth

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidAuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val delegate = AuthRepositoryImpl()

    override suspend fun login(email: String, password: String): Result<String> =
        delegate.login(email, password)
}
