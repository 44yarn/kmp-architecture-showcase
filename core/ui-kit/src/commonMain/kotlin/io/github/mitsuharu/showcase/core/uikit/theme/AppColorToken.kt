@file:Suppress("detekt.MagicNumber")

package io.github.mitsuharu.showcase.core.uikit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class AppColorToken(
    // Primary
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val inversePrimary: Color,
    val primaryFixed: Color,
    val onPrimaryFixed: Color,
    val primaryFixedDim: Color,
    val onPrimaryFixedVariant: Color,

    // Secondary
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val secondaryFixed: Color,
    val onSecondaryFixed: Color,
    val secondaryFixedDim: Color,
    val onSecondaryFixedVariant: Color,

    // Tertiary
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val tertiaryFixed: Color,
    val onTertiaryFixed: Color,
    val tertiaryFixedDim: Color,
    val onTertiaryFixedVariant: Color,

    // Error
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,

    // Neutrals / Surfaces
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,

    // Extended surfaces
    val surfaceDim: Color,
    val surfaceBright: Color,
    val surfaceContainerLowest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,

    // Inverse & outline
    val inverseSurface: Color,
    val inverseOnSurface: Color,
    val outline: Color,
    val outlineVariant: Color,

    // Misc
    val scrim: Color,
    val surfaceTint: Color,

    // App-specific
    val overlay: Color,
) {
    companion object
}

fun AppColorToken.Companion.light(): AppColorToken {
    return AppColorToken(
        primary = Color(0xFF6750A4),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFEADDFF),
        onPrimaryContainer = Color(0xFF21005D),
        inversePrimary = Color(0xFF6750A4),
        primaryFixed = Color(0xFFEADDFF),
        onPrimaryFixed = Color(0xFF21005D),
        primaryFixedDim = Color(0xFF6750A4),
        onPrimaryFixedVariant = Color(0xFF21005D),

        secondary = Color(0xFF625B71),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFE8DEF8),
        onSecondaryContainer = Color(0xFF1D192B),
        secondaryFixed = Color(0xFFE8DEF8),
        onSecondaryFixed = Color(0xFF1D192B),
        secondaryFixedDim = Color(0xFF625B71),
        onSecondaryFixedVariant = Color(0xFF1D192B),

        tertiary = Color(0xFF7D5260),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFFFD8E4),
        onTertiaryContainer = Color(0xFF31111D),
        tertiaryFixed = Color(0xFFFFD8E4),
        onTertiaryFixed = Color(0xFF31111D),
        tertiaryFixedDim = Color(0xFF7D5260),
        onTertiaryFixedVariant = Color(0xFF31111D),

        error = Color(0xFFB3261E),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFF9DEDC),
        onErrorContainer = Color(0xFF410E0B),

        background = Color(0xFFFFFBFE),
        onBackground = Color(0xFF1C1B1F),
        surface = Color(0xFFFFFBFE),
        onSurface = Color(0xFF1C1B1F),
        surfaceVariant = Color(0xFFE7E0EC),
        onSurfaceVariant = Color(0xFF49454F),

        surfaceDim = Color(0xFFEDE7F2),
        surfaceBright = Color(0xFFFFFFFF),
        surfaceContainerLowest = Color(0xFFFFFFFF),
        surfaceContainerLow = Color(0xFFFDF8FF),
        surfaceContainer = Color(0xFFFAF4FF),
        surfaceContainerHigh = Color(0xFFF6F0FA),
        surfaceContainerHighest = Color(0xFFF2ECF6),

        inverseSurface = Color(0xFF313033),
        inverseOnSurface = Color(0xFFE6E1E5),
        outline = Color(0xFF79747E),
        outlineVariant = Color(0xFFCAC4D0),

        scrim = Color(0xFF000000),
        surfaceTint = Color(0xFF6750A4),

        overlay = Color(0x99000000),
    )
}

fun AppColorToken.Companion.dark(): AppColorToken {
    return AppColorToken(
        primary = Color(0xFFD0BCFF),
        onPrimary = Color(0xFF381E72),
        primaryContainer = Color(0xFF4F378B),
        onPrimaryContainer = Color(0xFFEADDFF),
        inversePrimary = Color(0xFFD0BCFF),
        primaryFixed = Color(0xFF4F378B),
        onPrimaryFixed = Color(0xFFEADDFF),
        primaryFixedDim = Color(0xFFD0BCFF),
        onPrimaryFixedVariant = Color(0xFFEADDFF),

        secondary = Color(0xFFCCC2DC),
        onSecondary = Color(0xFF332D41),
        secondaryContainer = Color(0xFF4A4458),
        onSecondaryContainer = Color(0xFFE8DEF8),
        secondaryFixed = Color(0xFF4A4458),
        onSecondaryFixed = Color(0xFFE8DEF8),
        secondaryFixedDim = Color(0xFFCCC2DC),
        onSecondaryFixedVariant = Color(0xFFE8DEF8),

        tertiary = Color(0xFFEFB8C8),
        onTertiary = Color(0xFF492532),
        tertiaryContainer = Color(0xFF633B48),
        onTertiaryContainer = Color(0xFFFFD8E4),
        tertiaryFixed = Color(0xFF633B48),
        onTertiaryFixed = Color(0xFFFFD8E4),
        tertiaryFixedDim = Color(0xFFEFB8C8),
        onTertiaryFixedVariant = Color(0xFFFFD8E4),

        error = Color(0xFFF2B8B5),
        onError = Color(0xFF601410),
        errorContainer = Color(0xFF8C1D18),
        onErrorContainer = Color(0xFFF9DEDC),

        background = Color(0xFF1C1B1F),
        onBackground = Color(0xFFE6E1E5),
        surface = Color(0xFF1C1B1F),
        onSurface = Color(0xFFE6E1E5),
        surfaceVariant = Color(0xFF49454F),
        onSurfaceVariant = Color(0xFFCAC4D0),

        surfaceDim = Color(0xFF141318),
        surfaceBright = Color(0xFF26242A),
        surfaceContainerLowest = Color(0xFF0F0E13),
        surfaceContainerLow = Color(0xFF17161B),
        surfaceContainer = Color(0xFF1D1B21),
        surfaceContainerHigh = Color(0xFF232129),
        surfaceContainerHighest = Color(0xFF2B2930),

        inverseSurface = Color(0xFFE6E1E5),
        inverseOnSurface = Color(0xFF313033),
        outline = Color(0xFF938F99),
        outlineVariant = Color(0xFF49454F),

        scrim = Color(0xFF000000),
        surfaceTint = Color(0xFFD0BCFF),

        overlay = Color(0x99000000),
    )
}
