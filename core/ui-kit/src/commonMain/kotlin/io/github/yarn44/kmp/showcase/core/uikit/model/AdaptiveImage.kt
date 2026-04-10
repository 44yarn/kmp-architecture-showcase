package io.github.yarn44.kmp.showcase.core.uikit.model

sealed interface AdaptiveImage {
    data class Url(val url: String) : AdaptiveImage
    data class Resource(val resId: Int) : AdaptiveImage
}
