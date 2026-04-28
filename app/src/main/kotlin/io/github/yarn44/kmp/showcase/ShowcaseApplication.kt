package io.github.yarn44.kmp.showcase

import android.app.Application
import android.content.Context
import io.github.yarn44.kmp.showcase.di.ShowcaseAppComponent
import io.github.yarn44.kmp.showcase.di.create
import timber.log.Timber

class ShowcaseApplication : Application() {
    val appComponent: ShowcaseAppComponent by lazy {
        ShowcaseAppComponent::class.create(this)
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}

val Context.appComponent: ShowcaseAppComponent
    get() = (applicationContext as ShowcaseApplication).appComponent
