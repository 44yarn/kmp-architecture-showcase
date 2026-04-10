@file:Suppress("detekt.MagicNumber")

package io.github.mitsuharu.showcase.core.uikit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/*
Modeled after the Material 3 ColorScheme (Compose).
https://m3.material.io/styles/color/the-color-system/color-roles

-----------------------------
App background:            background            onBackground
Cards, bottom sheets, etc: surface               onSurface
Surface variants:          surfaceVariant        onSurfaceVariant
Brand color:               primary               onPrimary
Brand background:          primaryContainer      onPrimaryContainer

-----------------------------
<Brand colors>
primary:   main brand color
secondary: supporting color
tertiary:  accent / decorative color
*/

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
    val v = AppColorValues.Light
    return AppColorToken(
        primary = Color(v.PRIMARY),
        onPrimary = Color(v.ON_PRIMARY),
        primaryContainer = Color(v.PRIMARY_CONTAINER),
        onPrimaryContainer = Color(v.ON_PRIMARY_CONTAINER),
        inversePrimary = Color(v.INVERSE_PRIMARY),
        primaryFixed = Color(v.PRIMARY_FIXED),
        onPrimaryFixed = Color(v.ON_PRIMARY_FIXED),
        primaryFixedDim = Color(v.PRIMARY_FIXED_DIM),
        onPrimaryFixedVariant = Color(v.ON_PRIMARY_FIXED_VARIANT),
        secondary = Color(v.SECONDARY),
        onSecondary = Color(v.ON_SECONDARY),
        secondaryContainer = Color(v.SECONDARY_CONTAINER),
        onSecondaryContainer = Color(v.ON_SECONDARY_CONTAINER),
        secondaryFixed = Color(v.SECONDARY_FIXED),
        onSecondaryFixed = Color(v.ON_SECONDARY_FIXED),
        secondaryFixedDim = Color(v.SECONDARY_FIXED_DIM),
        onSecondaryFixedVariant = Color(v.ON_SECONDARY_FIXED_VARIANT),
        tertiary = Color(v.TERTIARY),
        onTertiary = Color(v.ON_TERTIARY),
        tertiaryContainer = Color(v.TERTIARY_CONTAINER),
        onTertiaryContainer = Color(v.ON_TERTIARY_CONTAINER),
        tertiaryFixed = Color(v.TERTIARY_FIXED),
        onTertiaryFixed = Color(v.ON_TERTIARY_FIXED),
        tertiaryFixedDim = Color(v.TERTIARY_FIXED_DIM),
        onTertiaryFixedVariant = Color(v.ON_TERTIARY_FIXED_VARIANT),
        error = Color(v.ERROR),
        onError = Color(v.ON_ERROR),
        errorContainer = Color(v.ERROR_CONTAINER),
        onErrorContainer = Color(v.ON_ERROR_CONTAINER),
        background = Color(v.BACKGROUND),
        onBackground = Color(v.ON_BACKGROUND),
        surface = Color(v.SURFACE),
        onSurface = Color(v.ON_SURFACE),
        surfaceVariant = Color(v.SURFACE_VARIANT),
        onSurfaceVariant = Color(v.ON_SURFACE_VARIANT),
        surfaceDim = Color(v.SURFACE_DIM),
        surfaceBright = Color(v.SURFACE_BRIGHT),
        surfaceContainerLowest = Color(v.SURFACE_CONTAINER_LOWEST),
        surfaceContainerLow = Color(v.SURFACE_CONTAINER_LOW),
        surfaceContainer = Color(v.SURFACE_CONTAINER),
        surfaceContainerHigh = Color(v.SURFACE_CONTAINER_HIGH),
        surfaceContainerHighest = Color(v.SURFACE_CONTAINER_HIGHEST),
        inverseSurface = Color(v.INVERSE_SURFACE),
        inverseOnSurface = Color(v.INVERSE_ON_SURFACE),
        outline = Color(v.OUTLINE),
        outlineVariant = Color(v.OUTLINE_VARIANT),
        scrim = Color(v.SCRIM),
        surfaceTint = Color(v.SURFACE_TINT),
        overlay = Color(v.OVERLAY),
    )
}

fun AppColorToken.Companion.dark(): AppColorToken {
    val v = AppColorValues.Dark
    return AppColorToken(
        primary = Color(v.PRIMARY),
        onPrimary = Color(v.ON_PRIMARY),
        primaryContainer = Color(v.PRIMARY_CONTAINER),
        onPrimaryContainer = Color(v.ON_PRIMARY_CONTAINER),
        inversePrimary = Color(v.INVERSE_PRIMARY),
        primaryFixed = Color(v.PRIMARY_FIXED),
        onPrimaryFixed = Color(v.ON_PRIMARY_FIXED),
        primaryFixedDim = Color(v.PRIMARY_FIXED_DIM),
        onPrimaryFixedVariant = Color(v.ON_PRIMARY_FIXED_VARIANT),
        secondary = Color(v.SECONDARY),
        onSecondary = Color(v.ON_SECONDARY),
        secondaryContainer = Color(v.SECONDARY_CONTAINER),
        onSecondaryContainer = Color(v.ON_SECONDARY_CONTAINER),
        secondaryFixed = Color(v.SECONDARY_FIXED),
        onSecondaryFixed = Color(v.ON_SECONDARY_FIXED),
        secondaryFixedDim = Color(v.SECONDARY_FIXED_DIM),
        onSecondaryFixedVariant = Color(v.ON_SECONDARY_FIXED_VARIANT),
        tertiary = Color(v.TERTIARY),
        onTertiary = Color(v.ON_TERTIARY),
        tertiaryContainer = Color(v.TERTIARY_CONTAINER),
        onTertiaryContainer = Color(v.ON_TERTIARY_CONTAINER),
        tertiaryFixed = Color(v.TERTIARY_FIXED),
        onTertiaryFixed = Color(v.ON_TERTIARY_FIXED),
        tertiaryFixedDim = Color(v.TERTIARY_FIXED_DIM),
        onTertiaryFixedVariant = Color(v.ON_TERTIARY_FIXED_VARIANT),
        error = Color(v.ERROR),
        onError = Color(v.ON_ERROR),
        errorContainer = Color(v.ERROR_CONTAINER),
        onErrorContainer = Color(v.ON_ERROR_CONTAINER),
        background = Color(v.BACKGROUND),
        onBackground = Color(v.ON_BACKGROUND),
        surface = Color(v.SURFACE),
        onSurface = Color(v.ON_SURFACE),
        surfaceVariant = Color(v.SURFACE_VARIANT),
        onSurfaceVariant = Color(v.ON_SURFACE_VARIANT),
        surfaceDim = Color(v.SURFACE_DIM),
        surfaceBright = Color(v.SURFACE_BRIGHT),
        surfaceContainerLowest = Color(v.SURFACE_CONTAINER_LOWEST),
        surfaceContainerLow = Color(v.SURFACE_CONTAINER_LOW),
        surfaceContainer = Color(v.SURFACE_CONTAINER),
        surfaceContainerHigh = Color(v.SURFACE_CONTAINER_HIGH),
        surfaceContainerHighest = Color(v.SURFACE_CONTAINER_HIGHEST),
        inverseSurface = Color(v.INVERSE_SURFACE),
        inverseOnSurface = Color(v.INVERSE_ON_SURFACE),
        outline = Color(v.OUTLINE),
        outlineVariant = Color(v.OUTLINE_VARIANT),
        scrim = Color(v.SCRIM),
        surfaceTint = Color(v.SURFACE_TINT),
        overlay = Color(v.OVERLAY),
    )
}
