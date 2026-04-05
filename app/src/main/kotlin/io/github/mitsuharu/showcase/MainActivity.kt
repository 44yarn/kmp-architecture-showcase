package io.github.mitsuharu.showcase

import android.os.Bundle
import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint
import io.github.mitsuharu.showcase.core.foundation.ActivityLauncher
import io.github.mitsuharu.showcase.core.uikit.theme.setContentWithTheme
import io.github.mitsuharu.showcase.navigation.ShowcaseNavGraph
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
