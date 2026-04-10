package io.github.yarn44.kmp.showcase.feature.info

import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.yarn44.kmp.showcase.core.uikit.theme.setContentWithTheme

@AndroidEntryPoint
class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentWithTheme {
            InfoScreen(onBack = { finish() })
        }
    }
}
