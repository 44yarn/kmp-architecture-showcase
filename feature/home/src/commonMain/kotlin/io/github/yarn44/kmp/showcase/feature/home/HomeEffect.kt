package io.github.yarn44.kmp.showcase.feature.home

sealed interface HomeEffect {
    data object NavigateToLogin : HomeEffect
}
