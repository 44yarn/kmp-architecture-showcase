package io.github.mitsuharu.showcase.feature.login

data class LoginUiState(
    val email: String = "demo@example.com",
    val password: String = "password",
    val isPasswordVisible: Boolean = false,
)
