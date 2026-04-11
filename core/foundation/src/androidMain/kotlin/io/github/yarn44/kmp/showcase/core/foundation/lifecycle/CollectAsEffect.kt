package io.github.yarn44.kmp.showcase.core.foundation.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * Lifecycle-aware collect helper for one-shot effects emitted from a
 * ViewModel (navigation, Toast, dialog triggers, etc.).
 *
 * The flow is collected while the host `LifecycleOwner` is at [state] or
 * higher. When the lifecycle drops below [state], collection is cancelled
 * and re-established the next time it comes back.
 *
 * ## Why not `LaunchedEffect(Unit) { flow.collect { ... } }`?
 *
 * A plain `LaunchedEffect(Unit)` follows the Composable's composition
 * lifecycle, not the `LifecycleOwner`'s. If an effect fires while the
 * screen is in the background (e.g. after `onStop`), a navigation
 * trigger can be delivered after the user has already moved on — a
 * classic source of duplicate navigation bugs.
 *
 * ## Usage
 *
 * ```kotlin
 * viewModel.effect.CollectAsEffect { effect ->
 *     when (effect) {
 *         is HomeEffect.NavigateToLogin -> onNavigateToLogin()
 *     }
 * }
 * ```
 *
 * Inspired by `collectOnLifecycle` in the sibling `android-ui-catalog`
 * project, adapted here for Composable call sites (it uses
 * [LocalLifecycleOwner] and wraps the collect in [LaunchedEffect]
 * instead of accepting an explicit `LifecycleOwner`).
 */
@Composable
fun <T> Flow<T>.CollectAsEffect(
    state: Lifecycle.State = Lifecycle.State.STARTED,
    action: suspend (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(this@CollectAsEffect, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(state) {
            collect(action)
        }
    }
}
