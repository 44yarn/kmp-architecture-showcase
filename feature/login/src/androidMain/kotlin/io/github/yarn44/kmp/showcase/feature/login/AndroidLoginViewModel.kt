package io.github.yarn44.kmp.showcase.feature.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.yarn44.kmp.showcase.core.data.auth.AuthRepository
import io.github.yarn44.kmp.showcase.core.data.preference.PreferenceStorage
import io.github.yarn44.kmp.showcase.core.ui.dialog.DialogPresenter
import io.github.yarn44.kmp.showcase.core.ui.indicator.IndicatorState
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
// Suppress ktlint's class-signature rule, which would collapse the constructor
// onto a single line. Wrapper VMs with DI parameters are easier to read when
// each parameter is on its own line.
@Suppress("ktlint:standard:class-signature")
class AndroidLoginViewModel @Inject constructor(
    authRepository: AuthRepository,
    preferenceStorage: PreferenceStorage,
) : ViewModel() {

    val dialogPresenter = DialogPresenter()
    val indicatorState = IndicatorState()

    private val commonViewModel = LoginViewModel(
        authRepository = authRepository,
        preferenceStorage = preferenceStorage,
        indicatorState = indicatorState,
        dialogPresenter = dialogPresenter,
    )

    val uiState: StateFlow<LoginUiState> = commonViewModel.uiState
    val effect = commonViewModel.effect

    val actions = LoginActions(
        onEmailChanged = commonViewModel::onEmailChanged,
        onPasswordChanged = commonViewModel::onPasswordChanged,
        onTogglePasswordVisibility = commonViewModel::onTogglePasswordVisibility,
        onLogin = commonViewModel::onLogin,
        onRandomEmail = commonViewModel::onRandomEmail,
        onLoginFailureDemo = commonViewModel::onLoginFailureDemo,
        onCancel = commonViewModel::onCancel,
        onInfo = commonViewModel::onInfo,
    )

    fun onGuestLogin() = commonViewModel.onGuestLogin()

    override fun onCleared() {
        commonViewModel.clear()
        super.onCleared()
    }
}
