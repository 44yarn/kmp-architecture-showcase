package io.github.yarn44.kmp.showcase.feature.info

import kotlinx.serialization.Serializable

/**
 * Compose Navigation destination for the Info screen. The showcase runs
 * on a Single-Activity architecture, so the Info screen is reached by
 * pushing this route onto the same `NavController` that hosts Login and
 * Home, rather than via `startActivity(Intent(context, InfoActivity::class.java))`.
 */
@Serializable
data object InfoRoute
