package io.github.mitsuharu.showcase.core.uikit.dialog

import androidx.compose.material3.AlertDialog
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
        title = { Text(uiState.title) },
        text = { Text(uiState.message) },
        confirmButton = {
            TextButton(onClick = { presenter.onPositive() }) {
                Text(uiState.positiveButton)
            }
        },
        dismissButton = uiState.negativeButton?.let { negativeText ->
            {
                TextButton(onClick = { presenter.onNegative() }) {
                    Text(negativeText)
                }
            }
        },
    )
}
