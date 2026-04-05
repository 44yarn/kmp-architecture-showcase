package io.github.mitsuharu.showcase.feature.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.rickclephas.kmp.observableviewmodel.coroutineScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorage
import io.github.mitsuharu.showcase.core.uikit.snackbar.SnackbarPresenter
import javax.inject.Inject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class AndroidHomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    preferenceStorage: PreferenceStorage,
    val snackbarPresenter: SnackbarPresenter,
) : ViewModel() {

    private val route = savedStateHandle.toRoute<HomeRoute>()

    private val commonViewModel = HomeViewModel(
        preferenceStorage = preferenceStorage,
        displayName = route.displayName,
        isGuest = route.isGuest,
    )

    val uiState: StateFlow<HomeUiState> = commonViewModel.uiState
    val effect = commonViewModel.effect

    val actions = HomeActions(
        onToggleRememberEmail = commonViewModel::onToggleRememberEmail,
        onLogout = commonViewModel::onLogout,
    )

    fun getSnackbarMessage(): String? = commonViewModel.getSnackbarMessage()

    override fun onCleared() {
        commonViewModel.viewModelScope.coroutineScope.cancel()
        super.onCleared()
    }
}
