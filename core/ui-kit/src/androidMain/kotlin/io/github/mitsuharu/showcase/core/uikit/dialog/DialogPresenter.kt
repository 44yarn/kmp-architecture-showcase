package io.github.mitsuharu.showcase.core.uikit.dialog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

class DialogPresenter @Inject constructor() {
    var dialogUiState: DialogUiState? by mutableStateOf(null)
        private set

    private var continuation: ((DialogResult) -> Unit)? = null

    suspend fun requestDialogResult(
        uiState: DialogUiState,
    ): DialogResult = suspendCancellableCoroutine { cont ->
        dialogUiState = uiState
        continuation = { result ->
            dialogUiState = null
            continuation = null
            cont.resume(result)
        }
        cont.invokeOnCancellation {
            dialogUiState = null
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
