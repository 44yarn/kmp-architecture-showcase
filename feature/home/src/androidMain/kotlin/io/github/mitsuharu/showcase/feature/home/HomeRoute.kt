package io.github.mitsuharu.showcase.feature.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeRoute(val displayName: String, val isGuest: Boolean,)
