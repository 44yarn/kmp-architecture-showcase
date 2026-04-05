package io.github.mitsuharu.showcase.core.uikit.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SnackbarView(
    presenter: SnackbarPresenter,
    modifier: Modifier = Modifier,
) {
    val uiState = presenter.snackbarUiState ?: return

    Snackbar(
        modifier = modifier,
        action = uiState.actionLabel?.let { label ->
            {
                TextButton(onClick = { presenter.hide() }) {
                    Text(label)
                }
            }
        },
        dismissAction = {
            TextButton(onClick = { presenter.hide() }) {
                Text("Dismiss")
            }
        },
    ) {
        Text(uiState.message)
    }
}
