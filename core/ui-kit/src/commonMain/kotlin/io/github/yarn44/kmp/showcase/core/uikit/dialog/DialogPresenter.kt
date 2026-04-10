package io.github.yarn44.kmp.showcase.core.uikit.dialog

import kotlin.coroutines.resume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.suspendCancellableCoroutine

class DialogPresenter {
    private val _uiState = MutableStateFlow<DialogUiState?>(null)
    val uiState: StateFlow<DialogUiState?> = _uiState.asStateFlow()

    private var continuation: ((DialogResult) -> Unit)? = null

    suspend fun requestDialogResult(
        uiState: DialogUiState,
    ): DialogResult = suspendCancellableCoroutine { cont ->
        _uiState.value = uiState
        continuation = { result ->
            _uiState.value = null
            continuation = null
            cont.resume(result)
        }
        cont.invokeOnCancellation {
            _uiState.value = null
            continuation = null
        }
    }

    fun onPositive() {
        continuation?.invoke(DialogResult.Positive)
    }

    fun onNegative() {
        continuation?.invoke(DialogResult.Negative)
    }

    fun onDismiss() {
        continuation?.invoke(DialogResult.Dismiss)
    }
}
