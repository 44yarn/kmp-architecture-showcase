package io.github.mitsuharu.showcase.feature.home

data class HomeUiState(
    val displayName: String = "",
    val isGuest: Boolean = false,
    val savedEmail: String? = null,
    val isRememberEmail: Boolean = false,
)
