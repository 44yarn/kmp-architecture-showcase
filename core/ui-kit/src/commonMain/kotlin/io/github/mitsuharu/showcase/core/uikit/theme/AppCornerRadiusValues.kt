@file:Suppress("detekt.MagicNumber")

package io.github.mitsuharu.showcase.core.uikit.theme

/**
 * コーナー半径トークンの生値（Float）。
 * Compose 型に依存しないため、iOS（Swift）からも shared.framework 経由で参照可能。
 * 単位は dp（Android）/ pt（iOS）。
 */
object AppCornerRadiusValues {
    const val SMALL = 10f
    const val MEDIUM = 18f
    const val LARGE = 24f
    const val EXTRA_LARGE = 28f
    const val FULL = 50f
}
