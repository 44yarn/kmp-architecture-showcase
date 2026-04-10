package io.github.mitsuharu.showcase.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorage
import io.github.mitsuharu.showcase.core.uikit.snackbar.SnackbarPresenter
import io.github.mitsuharu.showcase.core.uikit.snackbar.SnackbarUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
// Suppress ktlint's class-signature rule, which would collapse the constructor
// onto a single line. Wrapper VMs with DI parameters are easier to read when
// each parameter is on its own line.
@Suppress("ktlint:standard:class-signature")
class AndroidHomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    preferenceStorage: PreferenceStorage,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<HomeRoute>()

    private val commonViewModel = HomeViewModel(
        preferenceStorage = preferenceStorage,
        displayName = route.displayName,
        isGuest = route.isGuest,
        snackbarPresenter = SnackbarPresenter(),
    )

    val commonSnackbarPresenter: SnackbarPresenter = commonViewModel.snackbarPresenter
    val uiState: StateFlow<HomeUiState> = commonViewModel.uiState
    val effect = commonViewModel.effect

    val actions = HomeActions(
        onToggleRememberEmail = commonViewModel::onToggleRememberEmail,
        onLogout = commonViewModel::onLogout,
        onBack = {
            viewModelScope.launch {
                commonSnackbarPresenter.show(SnackbarUiState(message = "Use the Logout button to sign out"))
            }
        },
    )

    override fun onCleared() {
        commonViewModel.clear()
        super.onCleared()
    }
}
