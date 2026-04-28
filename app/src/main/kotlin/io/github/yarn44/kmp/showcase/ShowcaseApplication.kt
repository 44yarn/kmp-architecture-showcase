package io.github.yarn44.kmp.showcase

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.createGraphFactory
import io.github.yarn44.kmp.showcase.di.ShowcaseAppGraph
import timber.log.Timber

/**
 * Application entry point.
 *
 * Metro's [ShowcaseAppGraph] is instantiated lazily on first access so
 * that the graph factory runs against an already-initialized
 * `Application` instance. The graph is exposed through [appGraph] and
 * flows into [MainActivity] via the Application cast.
 */
class ShowcaseApplication : Application() {
    val appGraph: ShowcaseAppGraph by lazy {
        createGraphFactory<ShowcaseAppGraph.Factory>()
            .create(applicationContext = this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}

/**
 * Convenience accessor for the Metro graph from any [Context]. Called
 * from `MainActivity.onCreate` to pass the graph into `ShowcaseNavGraph`.
 */
val Context.appGraph: ShowcaseAppGraph
    get() = (applicationContext as ShowcaseApplication).appGraph
