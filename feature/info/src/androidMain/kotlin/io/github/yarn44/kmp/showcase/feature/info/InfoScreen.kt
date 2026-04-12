package io.github.yarn44.kmp.showcase.feature.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.yarn44.kmp.showcase.core.uikit.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Info",
                        style = MaterialTheme.typography.titleLarge,
                        color = AppTheme.colorToken.onBackground,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = AppTheme.colorToken.onBackground,
                        )
                    }
                },
            )
        },
        modifier = modifier,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = InfoContent.TITLE,
                style = MaterialTheme.typography.headlineMedium,
                color = AppTheme.colorToken.onBackground,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = InfoContent.DESCRIPTION,
                style = MaterialTheme.typography.bodyLarge,
                color = AppTheme.colorToken.onBackground,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Version ${InfoContent.VERSION}",
                style = MaterialTheme.typography.bodySmall,
                color = AppTheme.colorToken.onSurfaceVariant,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InfoScreenPreview() {
    AppTheme {
        InfoScreen(onBack = {})
    }
}
