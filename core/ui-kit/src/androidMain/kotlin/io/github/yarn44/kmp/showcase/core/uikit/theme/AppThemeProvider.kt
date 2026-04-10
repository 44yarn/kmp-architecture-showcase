package io.github.yarn44.kmp.showcase.core.uikit.theme

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun AppTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorToken = if (isDarkTheme) AppColorToken.dark() else AppColorToken.light()
    val colorScheme = if (isDarkTheme) appDarkColorScheme() else appLightColorScheme()

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.decorView.setBackgroundColor(colorToken.background.toArgb())
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !isDarkTheme
        }
    }

    CompositionLocalProvider(LocalColorToken provides colorToken) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MaterialTheme.typography.copy(
                titleLarge = AppTheme.typography.subheadline.bold,
            ),
            content = content,
        )
    }
}

fun ComponentActivity.setContentWithTheme(
    content: @Composable () -> Unit,
) {
    enableEdgeToEdge()
    setContent {
        AppTheme(content = content)
    }
}
