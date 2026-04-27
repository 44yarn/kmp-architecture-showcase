package io.github.yarn44.kmp.showcase.core.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

fun appLightColorScheme(): ColorScheme {
    val colorToken = AppColorToken.light()
    return lightColorScheme(
        primary = colorToken.primary,
        onPrimary = colorToken.onPrimary,
        primaryContainer = colorToken.primaryContainer,
        onPrimaryContainer = colorToken.onPrimaryContainer,
        inversePrimary = colorToken.inversePrimary,

        secondary = colorToken.secondary,
        onSecondary = colorToken.onSecondary,
        secondaryContainer = colorToken.secondaryContainer,
        onSecondaryContainer = colorToken.onSecondaryContainer,

        tertiary = colorToken.tertiary,
        onTertiary = colorToken.onTertiary,
        tertiaryContainer = colorToken.tertiaryContainer,
        onTertiaryContainer = colorToken.onTertiaryContainer,

        error = colorToken.error,
        onError = colorToken.onError,
        errorContainer = colorToken.errorContainer,
        onErrorContainer = colorToken.onErrorContainer,

        background = colorToken.background,
        onBackground = colorToken.onBackground,
        surface = colorToken.surface,
        onSurface = colorToken.onSurface,
        surfaceVariant = colorToken.surfaceVariant,
        onSurfaceVariant = colorToken.onSurfaceVariant,

        surfaceDim = colorToken.surfaceDim,
        surfaceBright = colorToken.surfaceBright,
        surfaceContainerLowest = colorToken.surfaceContainerLowest,
        surfaceContainerLow = colorToken.surfaceContainerLow,
        surfaceContainer = colorToken.surfaceContainer,
        surfaceContainerHigh = colorToken.surfaceContainerHigh,
        surfaceContainerHighest = colorToken.surfaceContainerHighest,

        inverseSurface = colorToken.inverseSurface,
        inverseOnSurface = colorToken.inverseOnSurface,
        outline = colorToken.outline,
        outlineVariant = colorToken.outlineVariant,
        scrim = colorToken.scrim,
        surfaceTint = colorToken.surfaceTint,
    )
}

fun appDarkColorScheme(): ColorScheme {
    val colorToken = AppColorToken.dark()
    return darkColorScheme(
        primary = colorToken.primary,
        onPrimary = colorToken.onPrimary,
        primaryContainer = colorToken.primaryContainer,
        onPrimaryContainer = colorToken.onPrimaryContainer,
        inversePrimary = colorToken.inversePrimary,

        secondary = colorToken.secondary,
        onSecondary = colorToken.onSecondary,
        secondaryContainer = colorToken.secondaryContainer,
        onSecondaryContainer = colorToken.onSecondaryContainer,

        tertiary = colorToken.tertiary,
        onTertiary = colorToken.onTertiary,
        tertiaryContainer = colorToken.tertiaryContainer,
        onTertiaryContainer = colorToken.onTertiaryContainer,

        error = colorToken.error,
        onError = colorToken.onError,
        errorContainer = colorToken.errorContainer,
        onErrorContainer = colorToken.onErrorContainer,

        background = colorToken.background,
        onBackground = colorToken.onBackground,
        surface = colorToken.surface,
        onSurface = colorToken.onSurface,
        surfaceVariant = colorToken.surfaceVariant,
        onSurfaceVariant = colorToken.onSurfaceVariant,

        surfaceDim = colorToken.surfaceDim,
        surfaceBright = colorToken.surfaceBright,
        surfaceContainerLowest = colorToken.surfaceContainerLowest,
        surfaceContainerLow = colorToken.surfaceContainerLow,
        surfaceContainer = colorToken.surfaceContainer,
        surfaceContainerHigh = colorToken.surfaceContainerHigh,
        surfaceContainerHighest = colorToken.surfaceContainerHighest,

        inverseSurface = colorToken.inverseSurface,
        inverseOnSurface = colorToken.inverseOnSurface,
        outline = colorToken.outline,
        outlineVariant = colorToken.outlineVariant,
        scrim = colorToken.scrim,
        surfaceTint = colorToken.surfaceTint,
    )
}
