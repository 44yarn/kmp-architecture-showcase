package io.github.yarn44.kmp.showcase.feature.login

sealed interface LoginEffect {
    data class NavigateToHome(val displayName: String, val isGuest: Boolean) : LoginEffect

    data object NavigateToInfo : LoginEffect
}
