package io.github.mitsuharu.showcase.feature.home

import io.github.mitsuharu.showcase.core.data.preference.PreferenceKey
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorage
import io.github.mitsuharu.showcase.core.foundation.KmpViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val preferenceStorage: PreferenceStorage, displayName: String, isGuest: Boolean,) : KmpViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            displayName = displayName,
            isGuest = isGuest,
        ),
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effect = Channel<HomeEffect>()
    val effect = _effect.receiveAsFlow()

    private var snackbarMessage: String? = null

    init {
        scope.launch {
            val savedEmail = preferenceStorage.getString(PreferenceKey.StringKey.SavedEmail)
            val rememberEmail = preferenceStorage.getBoolean(PreferenceKey.BooleanKey.RememberEmail)
            _uiState.update { it.copy(savedEmail = savedEmail ?: "", isRememberEmail = rememberEmail) }
        }
        scope.launch {
            delay(500L)
            snackbarMessage = "Welcome, $displayName!"
        }
    }

    fun onToggleRememberEmail() {
        scope.launch {
            val newValue = !_uiState.value.isRememberEmail
            _uiState.update { it.copy(isRememberEmail = newValue) }
            preferenceStorage.putBoolean(PreferenceKey.BooleanKey.RememberEmail, newValue)
            if (!newValue) {
                preferenceStorage.remove(PreferenceKey.StringKey.SavedEmail)
                _uiState.update { it.copy(savedEmail = "") }
            }
        }
    }

    fun onLogout() {
        scope.launch {
            _effect.send(HomeEffect.NavigateToLogin)
        }
    }

    fun getSnackbarMessage(): String? {
        val msg = snackbarMessage
        snackbarMessage = null
        return msg
    }
}
