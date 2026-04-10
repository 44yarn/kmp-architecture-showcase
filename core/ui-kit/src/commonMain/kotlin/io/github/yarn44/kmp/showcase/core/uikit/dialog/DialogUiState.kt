package io.github.yarn44.kmp.showcase.core.uikit.dialog

data class DialogUiState(
    val title: String = "",
    val message: String = "",
    val positiveButton: String = "OK",
    val negativeButton: String? = null,
)
