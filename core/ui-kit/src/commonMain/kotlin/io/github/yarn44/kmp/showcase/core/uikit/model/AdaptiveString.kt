package io.github.yarn44.kmp.showcase.core.uikit.model

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Unifies literal strings and localized [StringResource]s into a single
 * consumer-facing type.
 *
 * ## Why this exists
 *
 * Real-world projects routinely need to mix two kinds of text in the same
 * UI field (for example in [io.github.yarn44.kmp.showcase.core.uikit.dialog.DialogUiState]):
 *
 * - **Known error types** -> mapped to a **localized resource** by the UI
 *   layer (e.g. `is AuthException -> AdaptiveString(Res.string.login_invalid_credentials)`).
 * - **Unknown errors or server-provided text** -> carried as a **literal
 *   String** (e.g. `AdaptiveString("An unexpected error occurred.")`).
 *
 * Without a unified type, consumers would have to branch on `String` vs
 * `StringResource` everywhere the field is built or rendered.
 * [AdaptiveString] hides the distinction behind a single type, so ViewModels
 * can emit either form and the UI layer just calls [value] to obtain the
 * resolved string.
 *
 * ## Notes for iOS (SwiftUI) consumers
 *
 * The `@Composable val value` accessor is only usable from a Composable
 * context. For SwiftUI-based iOS consumers, a non-Composable `resolve()`
 * extension is provided in `iosMain` (see `AdaptiveString.ios.kt`).
 */
@Suppress("detekt.SpreadOperator")
class AdaptiveString {
    internal var text: String? = null
        private set

    internal var resource: StringResource? = null
        private set

    internal var formatArgs: Array<out Any>? = null
        private set

    /** Resolves the string in a Composable context. */
    val value: String
        @Composable get() {
            text?.let { return it }
            val res = resource ?: return ""
            val args = formatArgs ?: return stringResource(res)
            return stringResource(res, *args)
        }

    /** Holds a plain literal string. */
    constructor(text: String) {
        this.text = text
    }

    /** Holds a localized resource reference. */
    constructor(resource: StringResource) {
        this.resource = resource
    }

    /** Holds a localized resource reference with format arguments. */
    constructor(resource: StringResource, vararg formatArgs: Any) {
        this.resource = resource
        this.formatArgs = formatArgs
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AdaptiveString) return false
        if (text != other.text) return false
        if (resource != other.resource) return false
        if (formatArgs != null) {
            if (other.formatArgs == null) return false
            if (!formatArgs.contentEquals(other.formatArgs)) return false
        } else if (other.formatArgs != null) {
            return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = text?.hashCode() ?: 0
        result = 31 * result + (resource?.hashCode() ?: 0)
        result = 31 * result + (formatArgs?.contentHashCode() ?: 0)
        return result
    }
}
