package io.github.mitsuharu.showcase.core.uikit.snackbar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject

class SnackbarPresenter @Inject constructor() {
    var snackbarUiState: SnackbarUiState? by mutableStateOf(null)
        private set

    val isVisible: Boolean
        get() = snackbarUiState != null

    fun show(snackbar: SnackbarUiState) {
        snackbarUiState = snackbar
    }

    fun hide() {
        snackbarUiState = null
    }
}
