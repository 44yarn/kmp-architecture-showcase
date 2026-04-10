package io.github.yarn44.kmp.showcase.feature.login

data class LoginUiState(
    val email: String = "demo@example.com",
    val password: String = "password",
    val isPasswordVisible: Boolean = false,
)
