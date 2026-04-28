package io.github.yarn44.kmp.showcase.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.yarn44.kmp.showcase.core.data.preference.PreferenceKey
import io.github.yarn44.kmp.showcase.core.data.preference.PreferenceStorage
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
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class HomeViewModel(
    private val preferenceStorage: PreferenceStorage,
    @Assisted displayName: String,
    @Assisted isGuest: Boolean,
    val snackbarPresenter: SnackbarPresenter,
) : ViewModel() {

    val actions = HomeActions(
        onToggleRememberEmail = ::onToggleRememberEmail,
        onLogout = ::onLogout,
        onBack = {
            viewModelScope.launch {
                snackbarPresenter.show(SnackbarUiState(message = "Use the Logout button to sign out"))
            }
        },
    )

    private val _uiState = MutableStateFlow(
        HomeUiState(
            displayName = displayName,
            isGuest = isGuest,
        ),
    )

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _effect = Channel<HomeEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            val savedEmail = preferenceStorage.getOrNull(PreferenceKey.Auth.SavedEmail)
            val rememberEmail = preferenceStorage.getOrDefault(PreferenceKey.Auth.RememberEmail, true)
            _uiState.update { it.copy(savedEmail = savedEmail, isRememberEmail = rememberEmail) }
        }
        viewModelScope.launch {
            delay(500L)
            val message = if (isGuest) "Guest mode" else "Welcome, $displayName!"
            snackbarPresenter.show(SnackbarUiState(message = message))
        }
    }

    fun onToggleRememberEmail() {
        viewModelScope.launch {
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
        viewModelScope.launch {
            _effect.send(HomeEffect.NavigateToLogin)
        }
    }

    @me.tatarka.inject.annotations.AssistedFactory
    fun interface Factory {
        fun create(displayName: String, isGuest: Boolean): HomeViewModel
    }
}
