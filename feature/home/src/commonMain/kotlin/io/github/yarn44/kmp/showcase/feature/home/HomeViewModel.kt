package io.github.yarn44.kmp.showcase.feature.home

import io.github.yarn44.kmp.showcase.core.data.preference.PreferenceKey
import io.github.yarn44.kmp.showcase.core.data.preference.PreferenceStorage
import io.github.yarn44.kmp.showcase.core.foundation.KmpViewModel
import io.github.yarn44.kmp.showcase.core.ui.snackbar.SnackbarPresenter
import io.github.yarn44.kmp.showcase.core.ui.snackbar.SnackbarUiState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val preferenceStorage: PreferenceStorage,
    displayName: String,
    isGuest: Boolean,
    val snackbarPresenter: SnackbarPresenter,
) : KmpViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            displayName = displayName,
            isGuest = isGuest,
        ),
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Rendezvous channel: effects are UI side-effects that require a live
    // collector on the screen. Suspending the producer when no one is
    // listening is intentional — queuing would risk delivering a stale
    // navigation to the next screen.
    private val _effect = Channel<HomeEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        scope.launch {
            val savedEmail = preferenceStorage.getOrNull(PreferenceKey.Auth.SavedEmail)
            val rememberEmail = preferenceStorage.getOrDefault(PreferenceKey.Auth.RememberEmail, true)
            _uiState.update { it.copy(savedEmail = savedEmail, isRememberEmail = rememberEmail) }
        }
        scope.launch {
            delay(500L)
            val message = if (isGuest) "Guest mode" else "Welcome, $displayName!"
            snackbarPresenter.show(SnackbarUiState(message = message))
        }
    }

    fun onToggleRememberEmail() {
        scope.launch {
            val newValue = !_uiState.value.isRememberEmail
            _uiState.update { it.copy(isRememberEmail = newValue) }
            preferenceStorage.put(PreferenceKey.Auth.RememberEmail, newValue)
            if (!newValue) {
                preferenceStorage.remove(PreferenceKey.Auth.SavedEmail)
                _uiState.update { it.copy(savedEmail = null) }
            }
        }
    }

    fun onLogout() {
        scope.launch {
            _effect.send(HomeEffect.NavigateToLogin)
        }
    }
}
