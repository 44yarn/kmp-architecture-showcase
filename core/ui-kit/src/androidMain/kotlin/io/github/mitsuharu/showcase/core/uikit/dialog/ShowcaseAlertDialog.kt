package io.github.mitsuharu.showcase.core.uikit.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ShowcaseAlertDialog(
    presenter: DialogPresenter,
) {
    val uiState = presenter.dialogUiState ?: return

    AlertDialog(
        onDismissRequest = { presenter.onDismiss() },
        title = {
            Text(
                text = uiState.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Text(
                text = uiState.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        confirmButton = {
            TextButton(onClick = { presenter.onPositive() }) {
                Text(
                    text = uiState.positiveButton,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        dismissButton = uiState.negativeButton?.let { negativeText ->
            {
                TextButton(onClick = { presenter.onNegative() }) {
                    Text(
                        text = negativeText,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        },
    )
}
