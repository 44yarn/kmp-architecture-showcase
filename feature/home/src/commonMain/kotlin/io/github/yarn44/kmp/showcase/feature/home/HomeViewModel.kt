package io.github.yarn44.kmp.showcase.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
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

/**
 * Home screen view model, migrated to Metro's assisted injection in Stage 3.
 *
 * `displayName` and `isGuest` originate from the navigation route
 * ([HomeRoute]) and are therefore passed at construction time by
 * `Factory.create(displayName, isGuest)` from the Compose layer. The
 * factory is a [ManualViewModelAssistedFactory] rather than the auto
 * `ViewModelAssistedFactory` + `CreationExtras` variant because the route
 * decoding (`SavedStateHandle.toRoute<HomeRoute>()`) is Android-specific
 * and is performed one level up, in `ShowcaseNavGraph`, so the Compose
 * call site already has plain `String` / `Boolean` values to hand in.
 *
 * `snackbarPresenter` follows the same Q-Plan-2 (a) pattern as
 * `LoginViewModel`: a default argument so Metro provides a fresh
 * instance per view model and tests can still override it explicitly.
 */
@AssistedInject
class HomeViewModel(
    private val preferenceStorage: PreferenceStorage,
    @Assisted displayName: String,
    @Assisted isGuest: Boolean,
    val snackbarPresenter: SnackbarPresenter = SnackbarPresenter(),
) : ViewModel() {

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

    val actions: HomeActions = HomeActions(
        onToggleRememberEmail = ::onToggleRememberEmail,
        onLogout = ::onLogout,
        onBack = ::onBack,
    )

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

    /**
     * Android-only back-hint — shows a snackbar reminding the user to use
     * the Logout button instead of the system back gesture. iOS does not
     * trigger this path (SwiftUI's NavigationStack back is disabled while
     * on Home), so the message is kept as an English literal rather than
     * a localized `AdaptiveString`.
     */
    private fun onBack() {
        viewModelScope.launch {
            snackbarPresenter.show(SnackbarUiState(message = "Use the Logout button to sign out"))
        }
    }

    /**
     * Metro-generated assisted factory for [HomeViewModel]. The `create`
     * method is called from the Compose navigation layer via
     * `appGraph.homeViewModelFactory.create(displayName, isGuest)` with
     * values already decoded from [HomeRoute].
     */
    @AssistedFactory
    fun interface Factory {
        fun create(displayName: String, isGuest: Boolean): HomeViewModel
    }
}
