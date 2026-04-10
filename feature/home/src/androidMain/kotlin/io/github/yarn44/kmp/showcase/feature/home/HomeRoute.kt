package io.github.yarn44.kmp.showcase.feature.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeRoute(val displayName: String, val isGuest: Boolean,)
