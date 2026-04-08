package io.github.mitsuharu.showcase.core.uikit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@ConsistentCopyVisibility
@Immutable
data class AppTypography internal constructor(
    val title1: Style = styleOf(AppTypographyValues.Title1),
    val title2: Style = styleOf(AppTypographyValues.Title2),
    val title3: Style = styleOf(AppTypographyValues.Title3),
    val title4: Style = styleOf(AppTypographyValues.Title4),
    val headline: Style = styleOf(AppTypographyValues.Headline),
    val body: Style = styleOf(AppTypographyValues.Body),
    val callout: Style = styleOf(AppTypographyValues.Callout),
    val subheadline: Style = styleOf(AppTypographyValues.Subheadline),
    val footnote: Style = styleOf(AppTypographyValues.Footnote),
    val caption1: Style = styleOf(AppTypographyValues.Caption1),
    val caption2: Style = styleOf(AppTypographyValues.Caption2),
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

private fun styleOf(entry: TypographyEntry) = AppTypography.Style(
    fontSize = entry.fontSize.sp,
    lineHeight = entry.lineHeight.sp,
    letterSpacing = entry.letterSpacing?.sp ?: TextUnit.Unspecified,
)
