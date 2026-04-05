package io.github.mitsuharu.showcase.core.uikit.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalColorToken = staticCompositionLocalOf { AppColorToken.light() }
internal val LocalTypography = staticCompositionLocalOf { AppTypography() }

object AppTheme {
    val colorToken: AppColorToken
        @Composable
        @ReadOnlyComposable
        get() = LocalColorToken.current

    val typography: AppTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current

    val spacing: AppSpacing = AppSpacing
}
