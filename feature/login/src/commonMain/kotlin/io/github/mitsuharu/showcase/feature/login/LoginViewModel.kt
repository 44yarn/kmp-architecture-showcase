package io.github.mitsuharu.showcase.feature.login

import io.github.mitsuharu.showcase.core.data.auth.AuthRepository
import io.github.mitsuharu.showcase.core.data.preference.PreferenceKey
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorage
import io.github.mitsuharu.showcase.core.foundation.KmpViewModel
import io.github.mitsuharu.showcase.core.uikit.dialog.DialogPresenter
import io.github.mitsuharu.showcase.core.uikit.dialog.DialogResult
import io.github.mitsuharu.showcase.core.uikit.dialog.DialogUiState
import io.github.mitsuharu.showcase.core.uikit.indicator.IndicatorState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val preferenceStorage: PreferenceStorage,
    val indicatorState: IndicatorState,
    val dialogPresenter: DialogPresenter,
) : KmpViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    private var currentJob: Job? = null

    init {
        scope.launch {
            val savedEmail = preferenceStorage.getString(PreferenceKey.StringKey.SavedEmail)
            if (savedEmail != null) {
                _uiState.update { it.copy(email = savedEmail) }
            }
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun onLogin() {
        currentJob = scope.launch {
            indicatorState.runWithLoading {
                authRepository.login(_uiState.value.email, _uiState.value.password)
            }.onSuccess { displayName ->
                preferenceStorage.putString(
                    PreferenceKey.StringKey.SavedEmail,
                    _uiState.value.email,
                )
                _effect.send(LoginEffect.NavigateToHome(displayName, isGuest = false))
            }.onFailure {
                showLoginErrorDialog()
            }
        }
    }

    private suspend fun showLoginErrorDialog() {
        val result = dialogPresenter.requestDialogResult(
            DialogUiState(
                title = "Login Failed",
                message = "Invalid credentials. Would you like to continue as a guest?",
                positiveButton = "Guest Login",
                negativeButton = "Cancel",
            ),
        )
        if (result == DialogResult.Positive) {
            _effect.send(LoginEffect.NavigateToHome("Guest", isGuest = true))
        }
    }

    fun onRandomEmail() {
        val random = "user${(1000..9999).random()}@example.com"
        _uiState.update { it.copy(email = random) }
    }

    fun onLoginFailureDemo() {
        _uiState.update { it.copy(password = "error") }
        onLogin()
    }

    fun onCancel() {
        currentJob?.cancel()
        currentJob = null
    }

    fun onInfo() {
        scope.launch {
            _effect.send(LoginEffect.LaunchActivity)
        }
    }

    fun onGuestLogin() {
        scope.launch {
            _effect.send(LoginEffect.NavigateToHome("Guest", isGuest = true))
        }
    }
}
