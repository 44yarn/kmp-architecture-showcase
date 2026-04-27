package io.github.yarn44.kmp.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.yarn44.kmp.showcase.core.foundation.ActivityLauncher
import io.github.yarn44.kmp.showcase.core.ui.theme.setContentWithTheme
import io.github.yarn44.kmp.showcase.navigation.ShowcaseNavGraph
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var activityLauncher: ActivityLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentWithTheme {
            ShowcaseNavGraph(activityLauncher = activityLauncher)
        }
    }
}
