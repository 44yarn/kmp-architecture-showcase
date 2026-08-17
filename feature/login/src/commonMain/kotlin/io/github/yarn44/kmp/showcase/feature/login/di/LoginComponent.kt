package io.github.yarn44.kmp.showcase.feature.login.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.github.yarn44.kmp.showcase.core.ui.dialog.DialogPresenter
import io.github.yarn44.kmp.showcase.core.ui.indicator.IndicatorState
import io.github.yarn44.kmp.showcase.feature.login.LoginViewModel

@ContributesTo(AppScope::class)
interface LoginComponent {

    val loginViewModel: LoginViewModel

    @Provides
    fun provideIndicatorState(): IndicatorState = IndicatorState()

    @Provides
    fun provideDialogPresenter(): DialogPresenter = DialogPresenter()
}
