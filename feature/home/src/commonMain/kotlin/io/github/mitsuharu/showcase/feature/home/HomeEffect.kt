package io.github.mitsuharu.showcase.feature.home

sealed interface HomeEffect {
    data object NavigateToLogin : HomeEffect
}
