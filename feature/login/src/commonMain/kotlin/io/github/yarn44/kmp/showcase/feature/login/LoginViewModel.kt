package io.github.yarn44.kmp.showcase.feature.login

import io.github.yarn44.kmp.showcase.core.data.auth.AuthException
import io.github.yarn44.kmp.showcase.core.data.auth.AuthRepository
import io.github.yarn44.kmp.showcase.core.data.preference.PreferenceKey
import io.github.yarn44.kmp.showcase.core.data.preference.PreferenceStorage
import io.github.yarn44.kmp.showcase.core.foundation.KmpViewModel
import io.github.yarn44.kmp.showcase.core.uikit.dialog.DialogPresenter
import io.github.yarn44.kmp.showcase.core.uikit.dialog.DialogResult
import io.github.yarn44.kmp.showcase.core.uikit.dialog.DialogUiState
import io.github.yarn44.kmp.showcase.core.uikit.indicator.IndicatorState
import io.github.yarn44.kmp.showcase.core.uikit.model.AdaptiveString
import io.github.yarn44.kmp.showcase.feature.login.resources.Res
import io.github.yarn44.kmp.showcase.feature.login.resources.login_dialog_cancel
import io.github.yarn44.kmp.showcase.feature.login.resources.login_dialog_guest_login
import io.github.yarn44.kmp.showcase.feature.login.resources.login_failed_title
import io.github.yarn44.kmp.showcase.feature.login.resources.login_invalid_credentials
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

    // Rendezvous channel: effects are UI side-effects that require a live
    // collector on the screen. Suspending the producer when no one is
    // listening is intentional — queuing would risk delivering a stale
    // navigation to the next screen.
    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    private var currentJob: Job? = null
    private var passwordBeforeFailureDemo: String? = null

    init {
        scope.launch {
            val savedEmail = preferenceStorage.getOrNull(PreferenceKey.Auth.SavedEmail)
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
                saveEmailIfRemembered()
                _effect.send(LoginEffect.NavigateToHome(displayName, isGuest = false))
            }.onFailure { throwable ->
                showLoginErrorDialog(throwable)
            }
        }
    }

    private suspend fun saveEmailIfRemembered() {
        val rememberEmail = preferenceStorage.getOrDefault(
            PreferenceKey.Auth.RememberEmail,
            true,
        )
        if (rememberEmail) {
            preferenceStorage.put(PreferenceKey.Auth.SavedEmail, _uiState.value.email)
        }
    }

    /**
     * Showcases the [AdaptiveString] pattern: the dialog mixes localized
     * resources (title and buttons) with either a localized message
     * (for known error types) or a literal fallback (for unknown errors).
     *
     * All fields are the same [AdaptiveString] type, so the dialog does not
     * need to know whether each field came from a resource or a literal.
     * Type-based mapping from exceptions to user-facing text is the
     * responsibility of the UI layer (this ViewModel), not of the
     * exception itself — see [AuthException]'s doc for the rationale.
     */
    private suspend fun showLoginErrorDialog(throwable: Throwable) {
        val message = when (throwable) {
            // Known error type -> pick a localized resource.
            is AuthException -> AdaptiveString(Res.string.login_invalid_credentials)
            // Unknown error -> fall back to a plain literal.
            // (In a real project this could also come from a server
            // response body, e.g. an error_message field.)
            else -> AdaptiveString("An unexpected error occurred. Please try again later.")
        }
        val result = dialogPresenter.requestDialogResult(
            DialogUiState(
                title = AdaptiveString(Res.string.login_failed_title),
                message = message,
                positiveButton = AdaptiveString(Res.string.login_dialog_guest_login),
                negativeButton = AdaptiveString(Res.string.login_dialog_cancel),
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
        passwordBeforeFailureDemo = _uiState.value.password
        _uiState.update { it.copy(password = "error") }
        currentJob = scope.launch {
            indicatorState.runWithLoading {
                authRepository.login(_uiState.value.email, "error")
            }.onFailure { throwable ->
                restorePassword()
                showLoginErrorDialog(throwable)
            }
        }
    }

    fun onCancel() {
        currentJob?.cancel()
        currentJob = null
        restorePassword()
    }

    private fun restorePassword() {
        passwordBeforeFailureDemo?.let { saved ->
            _uiState.update { it.copy(password = saved) }
            passwordBeforeFailureDemo = null
        }
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
