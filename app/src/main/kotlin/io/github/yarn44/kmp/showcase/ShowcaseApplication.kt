package io.github.yarn44.kmp.showcase

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.createGraphFactory
import io.github.yarn44.kmp.showcase.di.ShowcaseAppGraph
import timber.log.Timber

class ShowcaseApplication : Application() {
    val appGraph: ShowcaseAppGraph by lazy {
        createGraphFactory<ShowcaseAppGraph.Factory>().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}

val Context.appGraph: ShowcaseAppGraph
    get() = (applicationContext as ShowcaseApplication).appGraph
