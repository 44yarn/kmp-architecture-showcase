package io.github.mitsuharu.showcase.feature.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.mitsuharu.showcase.core.uikit.snackbar.SnackbarUiState
import io.github.mitsuharu.showcase.core.uikit.snackbar.SnackbarView

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AndroidHomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler { /* disable system back */ }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.NavigateToLogin -> onNavigateToLogin()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getSnackbarMessage()?.let { message ->
            viewModel.snackbarPresenter.show(SnackbarUiState(message = message))
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.headlineLarge,
            )

            Text(
                text = if (uiState.isGuest) {
                    "${uiState.displayName} (Guest)"
                } else {
                    uiState.displayName
                },
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(8.dp))

            uiState.savedEmail?.let { email ->
                Text(
                    text = "Saved email: $email",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Remember Email")
                Switch(
                    checked = uiState.isRememberEmail,
                    onCheckedChange = { viewModel.actions.onToggleRememberEmail() },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = viewModel.actions.onLogout,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Logout")
            }
        }

        SnackbarView(
            presenter = viewModel.snackbarPresenter,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}
