package io.github.mitsuharu.showcase.feature.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mitsuharu.showcase.core.data.auth.AuthRepository
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorage
import io.github.mitsuharu.showcase.core.uikit.dialog.DialogPresenter
import io.github.mitsuharu.showcase.core.uikit.indicator.IndicatorState
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class AndroidLoginViewModel @Inject constructor(authRepository: AuthRepository, preferenceStorage: PreferenceStorage,) :
    ViewModel() {

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
