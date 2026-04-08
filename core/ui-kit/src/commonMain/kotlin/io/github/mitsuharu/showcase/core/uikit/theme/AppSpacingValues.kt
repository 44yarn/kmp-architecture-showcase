@file:Suppress("detekt.MagicNumber")

package io.github.mitsuharu.showcase.core.uikit.theme

/**
 * スペーシングトークンの生値（Float）。
 * Compose 型に依存しないため、iOS（Swift）からも shared.framework 経由で参照可能。
 * 単位は dp（Android）/ pt（iOS）。
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
