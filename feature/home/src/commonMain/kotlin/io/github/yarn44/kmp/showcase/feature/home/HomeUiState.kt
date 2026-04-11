package io.github.yarn44.kmp.showcase.feature.home

data class HomeUiState(
    val displayName: String = "",
    val isGuest: Boolean = false,
    val savedEmail: String? = null,
    val isRememberEmail: Boolean = true,
) {
    val screenTitle: String get() = if (isGuest) "Guest Home" else "Home"
}
