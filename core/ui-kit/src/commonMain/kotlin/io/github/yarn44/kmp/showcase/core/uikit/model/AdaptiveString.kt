package io.github.yarn44.kmp.showcase.core.uikit.model

sealed interface AdaptiveString {
    data class Literal(val text: String) : AdaptiveString
    data class Resource(val resId: Int, val formatArgs: List<Any> = emptyList()) : AdaptiveString

    companion object {
        fun of(text: String): AdaptiveString = Literal(text)
        fun ofRes(resId: Int, vararg formatArgs: Any): AdaptiveString =
            Resource(resId, formatArgs.toList())
    }
}
