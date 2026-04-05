package io.github.mitsuharu.showcase.feature.home

data class HomeActions(val onToggleRememberEmail: () -> Unit = {}, val onLogout: () -> Unit = {}, val onBack: () -> Unit = {},)
