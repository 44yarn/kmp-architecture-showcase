package io.github.yarn44.kmp.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import io.github.yarn44.kmp.showcase.core.ui.theme.setContentWithTheme
import io.github.yarn44.kmp.showcase.navigation.ShowcaseNavGraph

/**
 * Single activity for the Showcase app.
 *
 * The Metro graph is obtained from [ShowcaseApplication] via the
 * `Context.appGraph` extension and passed directly into [ShowcaseNavGraph]
 * so each screen can retrieve its ViewModel via the direct accessor pattern
 * (`appGraph.loginViewModel`, `appGraph.homeViewModelFactory`).
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentWithTheme {
            ShowcaseNavGraph(appGraph = appGraph)
        }
    }
}
