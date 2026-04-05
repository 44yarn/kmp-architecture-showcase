package io.github.mitsuharu.showcase.feature.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.mitsuharu.showcase.core.uikit.dialog.DialogResult
import io.github.mitsuharu.showcase.core.uikit.dialog.DialogUiState
import io.github.mitsuharu.showcase.core.uikit.dialog.ShowcaseAlertDialog

@Composable
fun LoginScreen(
    onNavigateToHome: (String, Boolean) -> Unit,
    onLaunchActivity: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AndroidLoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading by viewModel.indicatorState.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is LoginEffect.NavigateToHome -> onNavigateToHome(effect.displayName, effect.isGuest)
                is LoginEffect.LaunchActivity -> onLaunchActivity()
            }
        }
    }

    ShowcaseAlertDialog(presenter = viewModel.dialogPresenter)

    LoginContent(
        uiState = uiState,
        isLoading = isLoading,
        actions = viewModel.actions,
        onLoginFailureWithDialog = {
            viewModel.actions.onLoginFailureDemo()
        },
        modifier = modifier,
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    isLoading: Boolean,
    actions: LoginActions,
    onLoginFailureWithDialog: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
                text = "KMP Showcase",
                style = MaterialTheme.typography.headlineLarge,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = actions.onEmailChanged,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            OutlinedTextField(
                value = uiState.password,
                onValueChange = actions.onPasswordChanged,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (uiState.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = actions.onTogglePasswordVisibility) {
                        Text(if (uiState.isPasswordVisible) "Hide" else "Show")
                    }
                },
            )

            Button(
                onClick = actions.onLogin,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && uiState.email.isNotBlank() && uiState.password.isNotBlank(),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.height(20.dp).width(20.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text("Login")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                TextButton(onClick = actions.onRandomEmail) {
                    Text("Random Email")
                }
                TextButton(onClick = onLoginFailureWithDialog) {
                    Text("Login Failure")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                OutlinedButton(onClick = actions.onCancel) {
                    Text("Cancel")
                }
                OutlinedButton(onClick = actions.onInfo) {
                    Text("Information")
                }
            }
        }
    }
}
