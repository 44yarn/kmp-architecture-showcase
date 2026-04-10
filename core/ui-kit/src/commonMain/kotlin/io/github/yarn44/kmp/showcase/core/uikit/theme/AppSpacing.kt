@file:Suppress("detekt.MagicNumber")

package io.github.yarn44.kmp.showcase.core.uikit.theme

import androidx.compose.ui.unit.dp

/**
 * App-wide spacing tokens.
 *
 * Organizes in-component padding, gaps between elements, icon sizes, etc.
 *
 * NOTE: The current categorization (Padding / Gap / IconSize) and naming
 * (xxSmall..xLarge) is provisional. Once usage grows and adding values or
 * categorizing them becomes ambiguous, consider switching to a numeric
 * naming scheme (spacing4, spacing8, ...) or consolidating the categories.
 */
object AppSpacing {

    /** Padding: inner padding of components. */
    object Padding {
        val xxSmall = AppSpacingValues.Padding.XX_SMALL.dp
        val xSmall = AppSpacingValues.Padding.X_SMALL.dp
        val small = AppSpacingValues.Padding.SMALL.dp
        val medium = AppSpacingValues.Padding.MEDIUM.dp
        val large = AppSpacingValues.Padding.LARGE.dp
        val xLarge = AppSpacingValues.Padding.X_LARGE.dp
    }

    /** Gap: spacing between elements. */
    object Gap {
        val minimal = AppSpacingValues.Gap.MINIMAL.dp
        val small = AppSpacingValues.Gap.SMALL.dp
        val medium = AppSpacingValues.Gap.MEDIUM.dp
    }

    /** IconSize: standard icon sizes. */
    object IconSize {
        val small = AppSpacingValues.IconSize.SMALL.dp
        val medium = AppSpacingValues.IconSize.MEDIUM.dp
    }
}
