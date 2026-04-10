package io.github.yarn44.kmp.showcase.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import io.github.yarn44.kmp.showcase.ActivityLauncherImpl
import io.github.yarn44.kmp.showcase.core.foundation.ActivityLauncher

@Module
@InstallIn(ActivityComponent::class)
abstract class ActivityLauncherModule {
    @Binds
    abstract fun bindActivityLauncher(impl: ActivityLauncherImpl): ActivityLauncher
}
