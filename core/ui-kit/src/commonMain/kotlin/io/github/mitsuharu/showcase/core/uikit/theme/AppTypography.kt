package io.github.mitsuharu.showcase.core.uikit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@ConsistentCopyVisibility
@Immutable
data class AppTypography internal constructor(
    val title1: Style = with(AppTypographyValues.Title1) { Style.from(FONT_SIZE, LINE_HEIGHT, LETTER_SPACING) },
    val title2: Style = with(AppTypographyValues.Title2) { Style.from(FONT_SIZE, LINE_HEIGHT, LETTER_SPACING) },
    val title3: Style = with(AppTypographyValues.Title3) { Style.from(FONT_SIZE, LINE_HEIGHT, LETTER_SPACING) },
    val title4: Style = with(AppTypographyValues.Title4) { Style.from(FONT_SIZE, LINE_HEIGHT, LETTER_SPACING) },
    val headline: Style = with(AppTypographyValues.Headline) { Style.from(FONT_SIZE, LINE_HEIGHT, LETTER_SPACING) },
    val body: Style = with(AppTypographyValues.Body) { Style.from(FONT_SIZE, LINE_HEIGHT, LETTER_SPACING) },
    val callout: Style = with(AppTypographyValues.Callout) { Style.from(FONT_SIZE, LINE_HEIGHT, LETTER_SPACING) },
    val subheadline: Style = with(AppTypographyValues.Subheadline) { Style.from(FONT_SIZE, LINE_HEIGHT, LETTER_SPACING) },
    val footnote: Style = with(AppTypographyValues.Footnote) { Style.from(FONT_SIZE, LINE_HEIGHT, LETTER_SPACING) },
    val caption1: Style = with(AppTypographyValues.Caption1) { Style.from(FONT_SIZE, LINE_HEIGHT, LETTER_SPACING) },
    val caption2: Style = with(AppTypographyValues.Caption2) { Style.from(FONT_SIZE, LINE_HEIGHT, LETTER_SPACING) },
) {
    data class Style(
        val fontSize: TextUnit,
        val lineHeight: TextUnit,
        val letterSpacing: TextUnit = TextUnit.Unspecified,
    ) {
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

        companion object {
            fun from(
                fontSize: Float,
                lineHeight: Float,
                letterSpacing: Float?,
            ) = Style(
                fontSize = fontSize.sp,
                lineHeight = lineHeight.sp,
                letterSpacing = letterSpacing?.sp ?: TextUnit.Unspecified,
            )
        }
    }
}
