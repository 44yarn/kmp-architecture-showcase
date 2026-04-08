package io.github.mitsuharu.showcase.core.uikit.theme

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.mitsuharu.showcase.core.uikit.theme.utils.AppThemeSurface

// https://m3.material.io/styles/color/roles

@Suppress("detekt.MagicNumber")
@Composable
private fun Color.toHex(): String {
    val a = (alpha * 255).toInt().coerceIn(0, 255)
    val r = (red * 255).toInt().coerceIn(0, 255)
    val g = (green * 255).toInt().coerceIn(0, 255)
    val b = (blue * 255).toInt().coerceIn(0, 255)
    return "#%02X%02X%02X%02X".format(a, r, g, b)
}

data class ColorRole(
    val name: String,
    val background: Color,
    val content: Color
)

@Suppress("detekt.MagicNumber")
@Composable
private fun ColorRoleItem(modifier: Modifier, colorRole: ColorRole) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(4.dp)
            .background(colorRole.background)
            .padding(16.dp),
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = colorRole.name,
            color = colorRole.content,
            style = AppTheme.typography.footnote.regular,
        )
        Text(
            text = colorRole.background.toHex(),
            color = colorRole.content.copy(alpha = 0.5f),
            style = AppTheme.typography.caption1.regular,
        )
    }
}

@Suppress("detekt.MagicNumber")
@Composable
private fun ColorRoleRowSet(colorRoles: List<ColorRole>) {
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Max)
    ) {
        colorRoles.forEach { colorRole ->
            ColorRoleItem(Modifier.weight(1f), colorRole)
        }
    }
}

@Suppress(
    "detekt.MaximumLineLength",
    "detekt.ArgumentListWrapping",
    "detekt.LongMethod",
)
@Composable
private fun ColorRoleList(modifier: Modifier, color: AppColorToken) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        ColorRoleRowSet(
            listOf(
                ColorRole("primary / onPrimary", color.primary, color.onPrimary),
                ColorRole("secondary / onSecondary", color.secondary, color.onSecondary),
                ColorRole("tertiary / onTertiary", color.tertiary, color.onTertiary),
            )
        )
        ColorRoleRowSet(
            listOf(
                ColorRole("primaryContainer / onPrimaryContainer", color.primaryContainer, color.onPrimaryContainer),
                ColorRole("secondaryContainer / onSecondaryContainer", color.secondaryContainer, color.onSecondaryContainer),
                ColorRole("tertiaryContainer / onTertiaryContainer", color.tertiaryContainer, color.onTertiaryContainer),
            )
        )
        ColorRoleRowSet(
            listOf(
                ColorRole("primaryFixed / onPrimaryFixed", color.primaryFixed, color.onPrimaryFixed),
                ColorRole("secondaryFixed / onSecondaryFixed", color.secondaryFixed, color.onSecondaryFixed),
                ColorRole("tertiaryFixed / onTertiaryFixed", color.tertiaryFixed, color.onTertiaryFixed),
            )
        )
        ColorRoleRowSet(
            listOf(
                ColorRole("primaryFixedDim / onPrimaryFixedVariant", color.primaryFixedDim, color.onPrimaryFixedVariant),
                ColorRole("secondaryFixedDim / onSecondaryFixedVariant", color.secondaryFixedDim, color.onSecondaryFixedVariant),
                ColorRole("tertiaryFixedDim / onTertiaryFixedVariant", color.tertiaryFixedDim, color.onTertiaryFixedVariant),
            )
        )
        ColorRoleRowSet(
            listOf(
                ColorRole("background / onBackground", color.background, color.onBackground),
                ColorRole("surface / onSurface", color.surface, color.onSurface),
                ColorRole("surfaceVariant / onSurfaceVariant", color.surfaceVariant, color.onSurfaceVariant),
            )
        )
        ColorRoleRowSet(
            listOf(
                ColorRole("surfaceDim / onSurface", color.surfaceDim, color.onSurface),
                ColorRole("surfaceBright / onSurface", color.surfaceBright, color.onSurface),
                ColorRole("surfaceContainerLowest / onSurface", color.surfaceContainerLowest, color.onSurface),
            )
        )
        ColorRoleRowSet(
            listOf(
                ColorRole("surfaceContainerLow / onSurface", color.surfaceContainerLow, color.onSurface),
                ColorRole("surfaceContainer / onSurface", color.surfaceContainer, color.onSurface),
            )
        )
        ColorRoleRowSet(
            listOf(
                ColorRole("surfaceContainerHigh / onSurface", color.surfaceContainerHigh, color.onSurface),
                ColorRole("surfaceContainerHighest / onSurface", color.surfaceContainerHighest, color.onSurface)
            )
        )
        ColorRoleRowSet(
            listOf(
                ColorRole("error / onError", color.error, color.onError),
                ColorRole("errorContainer / onErrorContainer", color.errorContainer, color.onErrorContainer),
            )
        )
        ColorRoleRowSet(
            listOf(
                ColorRole("inversePrimary", color.inversePrimary, Color.White),
                ColorRole("inverseSurface / inverseOnSurface", color.inverseSurface, color.inverseOnSurface),
            )
        )
        ColorRoleRowSet(
            listOf(
                ColorRole("outline", color.outline, Color.Black),
                ColorRole("outlineVariant", color.outlineVariant, Color.Black)
            )
        )
        ColorRoleRowSet(
            listOf(
                ColorRole("scrim", color.scrim, Color.White),
                ColorRole("surfaceTint", color.surfaceTint, Color.White),
            )
        )
    }
}

@Preview(showBackground = true, heightDp = 1300)
@Composable
fun PreviewColorRoles() {
    AppThemeSurface {
        ColorRoleList(
            Modifier.background(Color.Black.copy(alpha = 0.1f)),
            AppColorToken.light(),
        )
    }
}

@Preview(showBackground = true, heightDp = 1300, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewColorRolesDark() {
    AppThemeSurface {
        ColorRoleList(
            Modifier.background(Color.Black.copy(alpha = 0.8f)),
            AppColorToken.dark(),
        )
    }
}
