package io.github.yarn44.kmp.showcase.core.ui.snackbar

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SnackbarPresenter {
    private val _uiState = MutableStateFlow<SnackbarUiState?>(null)
    val uiState: StateFlow<SnackbarUiState?> = _uiState.asStateFlow()

    /**
     * Show the snackbar and auto-hide it after [autoHideDuration].
     *
     * Calling this multiple times in quick succession is safe: the latest call's
     * snackbar replaces the previous one, and only the latest snackbar's auto-hide
     * timer can hide it (via [MutableStateFlow.compareAndSet]).
     */
    suspend fun show(snackbar: SnackbarUiState) {
        _uiState.value = snackbar
        delay(autoHideDuration)
        // Only hide if THIS snackbar is still the one being shown.
        // If show() was called again or hide() was called, leave the new state alone.
        _uiState.compareAndSet(expect = snackbar, update = null)
    }

    /** Manually dismiss the current snackbar (e.g. user swipe). */
    fun hide() {
        _uiState.value = null
    }

    private companion object {
        val autoHideDuration = 3.seconds
    }
}
