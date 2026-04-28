package io.github.yarn44.kmp.showcase.feature.login.di

import io.github.yarn44.kmp.showcase.core.ui.dialog.DialogPresenter
import io.github.yarn44.kmp.showcase.core.ui.indicator.IndicatorState
import io.github.yarn44.kmp.showcase.feature.login.LoginViewModel
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo

@ContributesTo(AppScope::class)
interface LoginComponent {

    val loginViewModel: LoginViewModel

    @Provides
    fun provideIndicatorState(): IndicatorState = IndicatorState()

    @Provides
    fun provideDialogPresenter(): DialogPresenter = DialogPresenter()
}
