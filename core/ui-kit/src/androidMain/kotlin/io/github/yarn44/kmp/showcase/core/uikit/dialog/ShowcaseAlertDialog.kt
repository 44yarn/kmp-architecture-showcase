package io.github.yarn44.kmp.showcase.core.uikit.dialog

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

    // A dialog without a positive button makes no sense in this showcase,
    // so bail out early if it is not provided.
    val positive = current.positiveButton ?: return
    val title = current.title
    val message = current.message
    val negative = current.negativeButton

    AlertDialog(
        onDismissRequest = { presenter.onDismiss() },
        title = if (title != null) {
            {
                Text(
                    text = title.value,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        } else {
            null
        },
        text = if (message != null) {
            {
                Text(
                    text = message.value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            null
        },
        confirmButton = {
            TextButton(onClick = { presenter.onPositive() }) {
                Text(
                    text = positive.value,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        },
        dismissButton = if (negative != null) {
            {
                TextButton(onClick = { presenter.onNegative() }) {
                    Text(
                        text = negative.value,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        } else {
            null
        },
    )
}
