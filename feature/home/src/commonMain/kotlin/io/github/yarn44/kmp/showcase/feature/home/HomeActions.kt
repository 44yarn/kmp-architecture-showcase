package io.github.yarn44.kmp.showcase.feature.home

data class HomeActions(
    val onToggleRememberEmail: () -> Unit = {
    },
    val onLogout: () -> Unit = {},
    val onBack: () -> Unit = {},
)
