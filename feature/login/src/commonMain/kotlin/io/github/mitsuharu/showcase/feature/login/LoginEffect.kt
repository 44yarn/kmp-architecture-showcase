package io.github.mitsuharu.showcase.feature.login

sealed interface LoginEffect {
    data class NavigateToHome(val displayName: String, val isGuest: Boolean) : LoginEffect

    data object LaunchActivity : LoginEffect
}
