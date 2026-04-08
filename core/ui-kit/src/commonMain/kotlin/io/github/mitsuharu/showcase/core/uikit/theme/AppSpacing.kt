@file:Suppress("detekt.MagicNumber")

package io.github.mitsuharu.showcase.core.uikit.theme

import androidx.compose.ui.unit.dp

/**
 * アプリ共通のスペーシングトークン。
 * コンポーネント内の padding・gap・アイコンサイズ等を体系化する。
 *
 * NOTE: 現在の分類（Padding / Gap / IconSize）と命名（xxSmall〜xLarge）は暫定。
 * 利用が増えて値の追加や用途の境界で迷いが生じたら、
 * 数値ベース命名（spacing4, spacing8 等）や分類の統合を検討する。
 */
object AppSpacing {

    /** Padding: コンポーネントの内側余白 */
    object Padding {
        val xxSmall = AppSpacingValues.Padding.XX_SMALL.dp
        val xSmall = AppSpacingValues.Padding.X_SMALL.dp
        val small = AppSpacingValues.Padding.SMALL.dp
        val medium = AppSpacingValues.Padding.MEDIUM.dp
        val large = AppSpacingValues.Padding.LARGE.dp
        val xLarge = AppSpacingValues.Padding.X_LARGE.dp
    }

    /** Gap: 要素間のスペーシング */
    object Gap {
        val minimal = AppSpacingValues.Gap.MINIMAL.dp
        val small = AppSpacingValues.Gap.SMALL.dp
        val medium = AppSpacingValues.Gap.MEDIUM.dp
    }

    /** IconSize: アイコンの標準サイズ */
    object IconSize {
        val small = AppSpacingValues.IconSize.SMALL.dp
        val medium = AppSpacingValues.IconSize.MEDIUM.dp
    }
}
