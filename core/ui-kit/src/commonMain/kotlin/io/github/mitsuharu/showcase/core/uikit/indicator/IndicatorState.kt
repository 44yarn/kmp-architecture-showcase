package io.github.mitsuharu.showcase.core.uikit.indicator

import androidx.compose.runtime.Stable
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import io.github.mitsuharu.showcase.core.foundation.resultHandling.runCatchingCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Stable
class IndicatorState {
    private val _isLoading = MutableStateFlow(false)

    @NativeCoroutinesState
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun startLoading() {
        _isLoading.value = true
    }

    fun stopLoading() {
        _isLoading.value = false
    }

    suspend fun <T> runWithLoading(block: suspend () -> Result<T>): Result<T> {
        startLoading()
        return try {
            block()
        } catch (e: Exception) {
            runCatchingCancellable { throw e }
        } finally {
            stopLoading()
        }
    }
}
