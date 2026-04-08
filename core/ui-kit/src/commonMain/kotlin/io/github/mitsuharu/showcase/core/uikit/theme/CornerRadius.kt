package io.github.mitsuharu.showcase.core.uikit.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class CornerRadius(val size: Dp) {
    Small(AppCornerRadiusValues.SMALL.dp),
    Medium(AppCornerRadiusValues.MEDIUM.dp),
    Large(AppCornerRadiusValues.LARGE.dp),
    ExtraLarge(AppCornerRadiusValues.EXTRA_LARGE.dp),
    Full(AppCornerRadiusValues.FULL.dp);

    val shape: RoundedCornerShape by lazy {
        RoundedCornerShape(size)
    }

    val topShape: RoundedCornerShape by lazy {
        RoundedCornerShape(topStart = size, topEnd = size)
    }

    val bottomShape: RoundedCornerShape by lazy {
        RoundedCornerShape(bottomStart = size, bottomEnd = size)
    }
}
