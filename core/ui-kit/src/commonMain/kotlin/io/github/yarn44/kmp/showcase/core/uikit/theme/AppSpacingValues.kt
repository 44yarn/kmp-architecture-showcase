@file:Suppress("detekt.MagicNumber")

package io.github.yarn44.kmp.showcase.core.uikit.theme

/**
 * Raw spacing token values as [Float]s.
 *
 * These values do not depend on any Compose type, so they can also be referenced
 * from iOS (Swift) via `shared.framework`. Units are dp (Android) / pt (iOS).
 */
object AppSpacingValues {

    object Padding {
        const val XX_SMALL = 4f
        const val X_SMALL = 8f
        const val SMALL = 12f
        const val MEDIUM = 16f
        const val LARGE = 20f
        const val X_LARGE = 24f
    }

    object Gap {
        const val MINIMAL = 2f
        const val SMALL = 8f
        const val MEDIUM = 12f
    }

    object IconSize {
        const val SMALL = 16f
        const val MEDIUM = 24f
    }
}
