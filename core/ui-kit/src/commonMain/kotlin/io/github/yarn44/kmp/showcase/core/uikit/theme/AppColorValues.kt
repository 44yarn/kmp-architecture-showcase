@file:Suppress("detekt.MagicNumber")

package io.github.yarn44.kmp.showcase.core.uikit.theme

/**
 * Raw color token values as ARGB hex [Long]s.
 *
 * These values do not depend on any Compose type, so they can also be referenced
 * from iOS (Swift) via `shared.framework`.
 */
object AppColorValues {

    object Light {
        // Primary
        const val PRIMARY = 0xFF6750A4L
        const val ON_PRIMARY = 0xFFFFFFFFL
        const val PRIMARY_CONTAINER = 0xFFEADDFFL
        const val ON_PRIMARY_CONTAINER = 0xFF21005DL
        const val INVERSE_PRIMARY = 0xFF6750A4L
        const val PRIMARY_FIXED = 0xFFEADDFFL
        const val ON_PRIMARY_FIXED = 0xFF21005DL
        const val PRIMARY_FIXED_DIM = 0xFF6750A4L
        const val ON_PRIMARY_FIXED_VARIANT = 0xFF21005DL

        // Secondary
        const val SECONDARY = 0xFF625B71L
        const val ON_SECONDARY = 0xFFFFFFFFL
        const val SECONDARY_CONTAINER = 0xFFE8DEF8L
        const val ON_SECONDARY_CONTAINER = 0xFF1D192BL
        const val SECONDARY_FIXED = 0xFFE8DEF8L
        const val ON_SECONDARY_FIXED = 0xFF1D192BL
        const val SECONDARY_FIXED_DIM = 0xFF625B71L
        const val ON_SECONDARY_FIXED_VARIANT = 0xFF1D192BL

        // Tertiary
        const val TERTIARY = 0xFF7D5260L
        const val ON_TERTIARY = 0xFFFFFFFFL
        const val TERTIARY_CONTAINER = 0xFFFFD8E4L
        const val ON_TERTIARY_CONTAINER = 0xFF31111DL
        const val TERTIARY_FIXED = 0xFFFFD8E4L
        const val ON_TERTIARY_FIXED = 0xFF31111DL
        const val TERTIARY_FIXED_DIM = 0xFF7D5260L
        const val ON_TERTIARY_FIXED_VARIANT = 0xFF31111DL

        // Error
        const val ERROR = 0xFFB3261EL
        const val ON_ERROR = 0xFFFFFFFFL
        const val ERROR_CONTAINER = 0xFFF9DEDCL
        const val ON_ERROR_CONTAINER = 0xFF410E0BL

        // Neutrals / Surfaces
        const val BACKGROUND = 0xFFFFFBFEL
        const val ON_BACKGROUND = 0xFF1C1B1FL
        const val SURFACE = 0xFFFFFBFEL
        const val ON_SURFACE = 0xFF1C1B1FL
        const val SURFACE_VARIANT = 0xFFE7E0ECL
        const val ON_SURFACE_VARIANT = 0xFF49454FL

        // Extended surfaces
        const val SURFACE_DIM = 0xFFEDE7F2L
        const val SURFACE_BRIGHT = 0xFFFFFFFFL
        const val SURFACE_CONTAINER_LOWEST = 0xFFFFFFFFL
        const val SURFACE_CONTAINER_LOW = 0xFFFDF8FFL
        const val SURFACE_CONTAINER = 0xFFFAF4FFL
        const val SURFACE_CONTAINER_HIGH = 0xFFF6F0FAL
        const val SURFACE_CONTAINER_HIGHEST = 0xFFF2ECF6L

        // Inverse & outline
        const val INVERSE_SURFACE = 0xFF313033L
        const val INVERSE_ON_SURFACE = 0xFFE6E1E5L
        const val OUTLINE = 0xFF79747EL
        const val OUTLINE_VARIANT = 0xFFCAC4D0L

        // Misc
        const val SCRIM = 0xFF000000L
        const val SURFACE_TINT = 0xFF6750A4L

        // App-specific
        const val OVERLAY = 0x99000000L
    }

    object Dark {
        // Primary
        const val PRIMARY = 0xFFD0BCFFL
        const val ON_PRIMARY = 0xFF381E72L
        const val PRIMARY_CONTAINER = 0xFF4F378BL
        const val ON_PRIMARY_CONTAINER = 0xFFEADDFFL
        const val INVERSE_PRIMARY = 0xFFD0BCFFL
        const val PRIMARY_FIXED = 0xFF4F378BL
        const val ON_PRIMARY_FIXED = 0xFFEADDFFL
        const val PRIMARY_FIXED_DIM = 0xFFD0BCFFL
        const val ON_PRIMARY_FIXED_VARIANT = 0xFFEADDFFL

        // Secondary
        const val SECONDARY = 0xFFCCC2DCL
        const val ON_SECONDARY = 0xFF332D41L
        const val SECONDARY_CONTAINER = 0xFF4A4458L
        const val ON_SECONDARY_CONTAINER = 0xFFE8DEF8L
        const val SECONDARY_FIXED = 0xFF4A4458L
        const val ON_SECONDARY_FIXED = 0xFFE8DEF8L
        const val SECONDARY_FIXED_DIM = 0xFFCCC2DCL
        const val ON_SECONDARY_FIXED_VARIANT = 0xFFE8DEF8L

        // Tertiary
        const val TERTIARY = 0xFFEFB8C8L
        const val ON_TERTIARY = 0xFF492532L
        const val TERTIARY_CONTAINER = 0xFF633B48L
        const val ON_TERTIARY_CONTAINER = 0xFFFFD8E4L
        const val TERTIARY_FIXED = 0xFF633B48L
        const val ON_TERTIARY_FIXED = 0xFFFFD8E4L
        const val TERTIARY_FIXED_DIM = 0xFFEFB8C8L
        const val ON_TERTIARY_FIXED_VARIANT = 0xFFFFD8E4L

        // Error
        const val ERROR = 0xFFF2B8B5L
        const val ON_ERROR = 0xFF601410L
        const val ERROR_CONTAINER = 0xFF8C1D18L
        const val ON_ERROR_CONTAINER = 0xFFF9DEDCL

        // Neutrals / Surfaces
        const val BACKGROUND = 0xFF1C1B1FL
        const val ON_BACKGROUND = 0xFFE6E1E5L
        const val SURFACE = 0xFF1C1B1FL
        const val ON_SURFACE = 0xFFE6E1E5L
        const val SURFACE_VARIANT = 0xFF49454FL
        const val ON_SURFACE_VARIANT = 0xFFCAC4D0L

        // Extended surfaces
        const val SURFACE_DIM = 0xFF141318L
        const val SURFACE_BRIGHT = 0xFF26242AL
        const val SURFACE_CONTAINER_LOWEST = 0xFF0F0E13L
        const val SURFACE_CONTAINER_LOW = 0xFF17161BL
        const val SURFACE_CONTAINER = 0xFF1D1B21L
        const val SURFACE_CONTAINER_HIGH = 0xFF232129L
        const val SURFACE_CONTAINER_HIGHEST = 0xFF2B2930L

        // Inverse & outline
        const val INVERSE_SURFACE = 0xFFE6E1E5L
        const val INVERSE_ON_SURFACE = 0xFF313033L
        const val OUTLINE = 0xFF938F99L
        const val OUTLINE_VARIANT = 0xFF49454FL

        // Misc
        const val SCRIM = 0xFF000000L
        const val SURFACE_TINT = 0xFFD0BCFFL

        // App-specific
        const val OVERLAY = 0x99000000L
    }
}
