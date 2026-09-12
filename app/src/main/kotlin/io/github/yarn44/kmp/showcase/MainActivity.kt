package io.github.yarn44.kmp.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import io.github.yarn44.kmp.showcase.core.ui.theme.setContentWithTheme
import io.github.yarn44.kmp.showcase.navigation.ShowcaseNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appGraph = applicationContext.appGraph
        setContentWithTheme {
            ShowcaseNavGraph(appGraph = appGraph)
        }
    }
}
