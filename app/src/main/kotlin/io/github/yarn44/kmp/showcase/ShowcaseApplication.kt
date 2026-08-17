package io.github.yarn44.kmp.showcase

import android.app.Application
import android.content.Context
import dev.zacsweers.metro.createGraphFactory
import io.github.yarn44.kmp.showcase.di.ShowcaseAppComponent
import timber.log.Timber

class ShowcaseApplication : Application() {
    val appComponent: ShowcaseAppComponent by lazy {
        createGraphFactory<ShowcaseAppComponent.Factory>().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}

val Context.appComponent: ShowcaseAppComponent
    get() = (applicationContext as ShowcaseApplication).appComponent
