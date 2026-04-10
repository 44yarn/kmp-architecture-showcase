package io.github.mitsuharu.showcase.core.uikit.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ShowcaseAlertDialog(
    presenter: DialogPresenter,
) {
    val uiState by presenter.uiState.collectAsStateWithLifecycle()
    val current = uiState ?: return

    AlertDialog(
        onDismissRequest = { presenter.onDismiss() },
        title = {
            Text(
                text = current.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        text = {
            Text(
                text = current.message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        confirmButton = {
            TextButton(onClick = { presenter.onPositive() }) {
                Text(
                    text = current.positiveButton,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        dismissButton = current.negativeButton?.let { negativeText ->
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
