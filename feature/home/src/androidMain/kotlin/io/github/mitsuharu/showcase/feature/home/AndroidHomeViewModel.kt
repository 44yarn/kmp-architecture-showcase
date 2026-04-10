package io.github.mitsuharu.showcase.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorage
import io.github.mitsuharu.showcase.core.uikit.snackbar.SnackbarPresenter
import io.github.mitsuharu.showcase.core.uikit.snackbar.SnackbarUiState
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
// ktlint の class-signature rule がコンストラクタを 1 行に折り畳もうとするため抑制。
// DI を持つ wrapper VM は縦に並べた方が読みやすい。
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
            commonSnackbarPresenter.show(SnackbarUiState(message = "Use the Logout button to sign out"))
        },
    )

    override fun onCleared() {
        commonViewModel.clear()
        super.onCleared()
    }
}
