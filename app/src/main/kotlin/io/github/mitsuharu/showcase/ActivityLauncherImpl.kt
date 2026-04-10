package io.github.mitsuharu.showcase

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ActivityContext
import io.github.mitsuharu.showcase.core.foundation.ActivityLauncher
import io.github.mitsuharu.showcase.feature.info.InfoActivity
import javax.inject.Inject

// Suppress ktlint's class-signature rule. Without this, it splits an
// annotated class declaration awkwardly into three lines
// (class / @Inject / constructor); we prefer a natural multi-line form.
@Suppress("ktlint:standard:class-signature")
class ActivityLauncherImpl @Inject constructor(
    @ActivityContext private val context: Context,
) : ActivityLauncher {
    override fun launch(target: ActivityLauncher.Target) {
        when (target) {
            ActivityLauncher.Target.Info -> {
                context.startActivity(Intent(context, InfoActivity::class.java))
            }
        }
    }
}
