package io.github.yarn44.kmp.showcase.core.uikit.theme.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.yarn44.kmp.showcase.core.uikit.theme.AppTheme

@Composable
fun AppThemeSurface(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    AppTheme(
        isDarkTheme = isDarkTheme,
    ) {
        Surface(
            modifier = modifier,
            color = color,
        ) {
            content()
        }
    }
}
