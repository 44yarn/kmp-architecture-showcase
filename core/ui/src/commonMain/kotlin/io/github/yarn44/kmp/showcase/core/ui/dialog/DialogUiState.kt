package io.github.yarn44.kmp.showcase.core.ui.dialog

import io.github.yarn44.kmp.showcase.core.ui.adaptive.AdaptiveString

/**
 * Pure UI state for an alert dialog.
 *
 * Each text field is an [AdaptiveString] so that callers (typically a
 * ViewModel) can freely mix localized resources and plain literal strings
 * without branching. Consumers of this state (e.g. the dialog Composable)
 * simply resolve each field via [AdaptiveString.value] in a Composable
 * scope, or via the iOS-specific resolve extension from SwiftUI.
 */
data class DialogUiState(
    val title: AdaptiveString? = null,
    val message: AdaptiveString? = null,
    val positiveButton: AdaptiveString? = null,
    val negativeButton: AdaptiveString? = null,
)
