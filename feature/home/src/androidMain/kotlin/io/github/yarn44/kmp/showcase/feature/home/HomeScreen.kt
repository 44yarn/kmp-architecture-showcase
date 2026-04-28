package io.github.yarn44.kmp.showcase.feature.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.yarn44.kmp.showcase.core.foundation.lifecycle.CollectAsEffect
import io.github.yarn44.kmp.showcase.core.ui.snackbar.SnackbarView
import io.github.yarn44.kmp.showcase.core.ui.theme.AppTheme

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler(onBack = viewModel.actions.onBack)

    viewModel.effect.CollectAsEffect { effect ->
        when (effect) {
            is HomeEffect.NavigateToLogin -> onNavigateToLogin()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        HomeContent(
            uiState = uiState,
            actions = viewModel.actions,
        )

        SnackbarView(presenter = viewModel.snackbarPresenter)
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    actions: HomeActions,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = uiState.screenTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = AppTheme.colorToken.onBackground,
            )

            Text(
                text = "Hello, ${uiState.displayName}!",
                style = MaterialTheme.typography.bodyLarge,
                color = AppTheme.colorToken.onBackground,
            )

            uiState.savedEmail?.let { savedEmail ->
                Text(
                    text = "Saved email: $savedEmail",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppTheme.colorToken.outline,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "Remember Email",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppTheme.colorToken.onBackground,
                )
                Switch(
                    checked = uiState.isRememberEmail,
                    onCheckedChange = { actions.onToggleRememberEmail() },
                )
            }

            OutlinedButton(
                onClick = actions.onLogout,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Logout",
                    style = MaterialTheme.typography.labelLarge,
                    color = AppTheme.colorToken.primary,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    AppTheme {
        HomeContent(
            uiState = HomeUiState(
                displayName = "Demo User",
                isGuest = false,
                savedEmail = "demo@example.com",
                isRememberEmail = true,
            ),
            actions = HomeActions(),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeContentGuestPreview() {
    AppTheme {
        HomeContent(
            uiState = HomeUiState(
                displayName = "Guest",
                isGuest = true,
                savedEmail = null,
                isRememberEmail = false,
            ),
            actions = HomeActions(),
        )
    }
}
