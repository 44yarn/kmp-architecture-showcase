package io.github.mitsuharu.showcase.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.github.mitsuharu.showcase.ActivityLauncherImpl
import io.github.mitsuharu.showcase.core.foundation.ActivityLauncher

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityLauncherModule {
    @Binds
    abstract fun bindActivityLauncher(impl: ActivityLauncherImpl): ActivityLauncher
}
