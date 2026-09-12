package io.github.yarn44.kmp.showcase.feature.home.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.github.yarn44.kmp.showcase.core.ui.snackbar.SnackbarPresenter
import io.github.yarn44.kmp.showcase.feature.home.HomeViewModel

@ContributesTo(AppScope::class)
interface HomeProviders {

    val homeViewModelFactory: HomeViewModel.Factory

    @Provides
    fun provideSnackbarPresenter(): SnackbarPresenter = SnackbarPresenter()
}
