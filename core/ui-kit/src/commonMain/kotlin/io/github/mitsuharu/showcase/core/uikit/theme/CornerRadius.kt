package io.github.mitsuharu.showcase.core.uikit.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class CornerRadius(val size: Dp) {
    Small(10.dp),
    Medium(18.dp),
    Large(24.dp),
    ExtraLarge(28.dp),
    Full(50.dp);

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
