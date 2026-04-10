package io.github.yarn44.kmp.showcase.feature.home

data class HomeUiState(
    val displayName: String = "",
    val isGuest: Boolean = false,
    val savedEmail: String = "",
    val isRememberEmail: Boolean = false,
) {
    val screenTitle: String get() = if (isGuest) "Guest Home" else "Home"
}
