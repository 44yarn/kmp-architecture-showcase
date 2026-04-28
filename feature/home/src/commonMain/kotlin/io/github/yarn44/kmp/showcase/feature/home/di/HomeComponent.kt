package io.github.yarn44.kmp.showcase.feature.home.di

import io.github.yarn44.kmp.showcase.core.ui.snackbar.SnackbarPresenter
import io.github.yarn44.kmp.showcase.feature.home.HomeViewModel
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface HomeComponent {

    val homeViewModelFactory: HomeViewModel.Factory

    @Provides
    fun provideSnackbarPresenter(): SnackbarPresenter = SnackbarPresenter()
}
