package io.github.yarn44.kmp.showcase.feature.login

sealed interface LoginEffect {
    data class NavigateToHome(val displayName: String, val isGuest: Boolean) : LoginEffect

    /**
     * Triggered when the user taps the Information button. The Android UI
     * pushes the Info destination onto the shared `NavController`, and the
     * iOS UI pushes it onto the SwiftUI `NavigationStack`. Before the
     * Single-Activity refactor this was called `LaunchActivity` because it
     * previously kicked off `InfoActivity` via `Context.startActivity(...)`.
     */
    data object NavigateToInfo : LoginEffect
}
