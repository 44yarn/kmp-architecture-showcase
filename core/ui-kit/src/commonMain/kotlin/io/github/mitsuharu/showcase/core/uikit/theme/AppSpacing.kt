@file:Suppress("detekt.MagicNumber")

package io.github.mitsuharu.showcase.core.uikit.theme

import androidx.compose.ui.unit.dp

object AppSpacing {

    /** Padding: コンポーネントの内側余白 */
    object Padding {
        val xxSmall = 4.dp
        val xSmall = 8.dp
        val small = 12.dp
        val medium = 16.dp
        val large = 20.dp
        val xLarge = 24.dp
    }

    /** Gap: 要素間のスペーシング */
    object Gap {
        val minimal = 2.dp
        val small = 8.dp
        val medium = 12.dp
    }

    /** IconSize: アイコンの標準サイズ */
    object IconSize {
        val small = 16.dp
        val medium = 24.dp
    }
}
