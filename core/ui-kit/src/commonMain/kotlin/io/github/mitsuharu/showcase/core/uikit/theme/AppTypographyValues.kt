@file:Suppress("detekt.MagicNumber")

package io.github.mitsuharu.showcase.core.uikit.theme

/**
 * フォントトークンの生値（Float）。
 * Compose 型に依存しないため、iOS（Swift）からも shared.framework 経由で参照可能。
 *
 * fontSize / lineHeight の単位は sp（Android）/ pt（iOS）。
 * LETTER_SPACING は nullable のため const 不可（null = Compose の TextUnit.Unspecified に対応）。
 */
object AppTypographyValues {

    object Title1 {
        const val FONT_SIZE = 34f
        const val LINE_HEIGHT = 41f
        val LETTER_SPACING: Float? = 0.37f
    }

    object Title2 {
        const val FONT_SIZE = 28f
        const val LINE_HEIGHT = 34f
        val LETTER_SPACING: Float? = 0.36f
    }

    object Title3 {
        const val FONT_SIZE = 22f
        const val LINE_HEIGHT = 28f
        val LETTER_SPACING: Float? = 0.35f
    }

    object Title4 {
        const val FONT_SIZE = 20f
        const val LINE_HEIGHT = 24f
        val LETTER_SPACING: Float? = 0.38f
    }

    object Headline {
        const val FONT_SIZE = 17f
        const val LINE_HEIGHT = 22f
        val LETTER_SPACING: Float? = -0.41f
    }

    object Body {
        const val FONT_SIZE = 17f
        const val LINE_HEIGHT = 22f
        val LETTER_SPACING: Float? = -0.41f
    }

    object Callout {
        const val FONT_SIZE = 16f
        const val LINE_HEIGHT = 21f
        val LETTER_SPACING: Float? = -0.32f
    }

    object Subheadline {
        const val FONT_SIZE = 15f
        const val LINE_HEIGHT = 20f
        val LETTER_SPACING: Float? = -0.24f
    }

    object Footnote {
        const val FONT_SIZE = 13f
        const val LINE_HEIGHT = 18f
        val LETTER_SPACING: Float? = -0.08f
    }

    object Caption1 {
        const val FONT_SIZE = 12f
        const val LINE_HEIGHT = 16f
        val LETTER_SPACING: Float? = null
    }

    object Caption2 {
        const val FONT_SIZE = 11f
        const val LINE_HEIGHT = 13f
        val LETTER_SPACING: Float? = 0.07f
    }
}
