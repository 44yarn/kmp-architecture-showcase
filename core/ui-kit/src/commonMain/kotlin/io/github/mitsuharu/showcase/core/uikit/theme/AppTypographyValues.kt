@file:Suppress("detekt.MagicNumber")

package io.github.mitsuharu.showcase.core.uikit.theme

/**
 * Raw font token values as [Float]s.
 *
 * These values do not depend on any Compose type, so they can also be referenced
 * from iOS (Swift) via `shared.framework`.
 *
 * `fontSize` / `lineHeight` are in sp (Android) / pt (iOS).
 * `letterSpacing` is nullable, so it cannot be declared as `const`
 * (`null` maps to Compose's `TextUnit.Unspecified`).
 */
/** Shared properties of each font style object. */
interface TypographyEntry {
    val fontSize: Float
    val lineHeight: Float
    val letterSpacing: Float?
}

object AppTypographyValues {

    object Title1 : TypographyEntry {
        override val fontSize = 34f
        override val lineHeight = 41f
        override val letterSpacing: Float? = 0.37f
    }

    object Title2 : TypographyEntry {
        override val fontSize = 28f
        override val lineHeight = 34f
        override val letterSpacing: Float? = 0.36f
    }

    object Title3 : TypographyEntry {
        override val fontSize = 22f
        override val lineHeight = 28f
        override val letterSpacing: Float? = 0.35f
    }

    object Title4 : TypographyEntry {
        override val fontSize = 20f
        override val lineHeight = 24f
        override val letterSpacing: Float? = 0.38f
    }

    object Headline : TypographyEntry {
        override val fontSize = 17f
        override val lineHeight = 22f
        override val letterSpacing: Float? = -0.41f
    }

    object Body : TypographyEntry {
        override val fontSize = 17f
        override val lineHeight = 22f
        override val letterSpacing: Float? = -0.41f
    }

    object Callout : TypographyEntry {
        override val fontSize = 16f
        override val lineHeight = 21f
        override val letterSpacing: Float? = -0.32f
    }

    object Subheadline : TypographyEntry {
        override val fontSize = 15f
        override val lineHeight = 20f
        override val letterSpacing: Float? = -0.24f
    }

    object Footnote : TypographyEntry {
        override val fontSize = 13f
        override val lineHeight = 18f
        override val letterSpacing: Float? = -0.08f
    }

    object Caption1 : TypographyEntry {
        override val fontSize = 12f
        override val lineHeight = 16f
        override val letterSpacing: Float? = null
    }

    object Caption2 : TypographyEntry {
        override val fontSize = 11f
        override val lineHeight = 13f
        override val letterSpacing: Float? = 0.07f
    }
}
