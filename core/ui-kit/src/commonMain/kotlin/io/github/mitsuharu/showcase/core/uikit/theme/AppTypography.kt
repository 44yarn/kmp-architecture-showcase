package io.github.mitsuharu.showcase.core.uikit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Suppress("detekt.MagicNumber")
@ConsistentCopyVisibility
@Immutable
data class AppTypography internal constructor(
    val title1: Style = Style(34.sp, 41.sp, 0.37.sp),
    val title2: Style = Style(28.sp, 34.sp, 0.36.sp),
    val title3: Style = Style(22.sp, 28.sp, 0.35.sp),
    val title4: Style = Style(20.sp, 24.sp, 0.38.sp),
    val headline: Style = Style(17.sp, 22.sp, (-0.41).sp),
    val body: Style = Style(17.sp, 22.sp, (-0.41).sp),
    val callout: Style = Style(16.sp, 21.sp, (-0.32).sp),
    val subheadline: Style = Style(15.sp, 20.sp, (-0.24).sp),
    val footnote: Style = Style(13.sp, 18.sp, (-0.08).sp),
    val caption1: Style = Style(12.sp, 16.sp),
    val caption2: Style = Style(11.sp, 13.sp, 0.07.sp),
) {
    data class Style(val fontSize: TextUnit, val lineHeight: TextUnit, val letterSpacing: TextUnit = TextUnit.Unspecified,) {
        val regular: TextStyle = TextStyle(
            fontSize = fontSize,
            lineHeight = lineHeight,
            letterSpacing = letterSpacing,
        )

        val bold: TextStyle = TextStyle(
            fontSize = fontSize,
            lineHeight = lineHeight,
            letterSpacing = letterSpacing,
            fontWeight = FontWeight.Bold,
        )
    }
}
