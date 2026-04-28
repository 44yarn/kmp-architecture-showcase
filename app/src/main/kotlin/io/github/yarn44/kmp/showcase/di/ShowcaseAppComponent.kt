package io.github.yarn44.kmp.showcase.di

import android.content.Context
import io.github.yarn44.kmp.showcase.feature.home.HomeViewModel
import io.github.yarn44.kmp.showcase.feature.login.LoginViewModel
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.MergeComponent
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@MergeComponent(AppScope::class)
@SingleIn(AppScope::class)
abstract class ShowcaseAppComponent(
    @get:Provides val applicationContext: Context,
) {
    abstract val loginViewModel: LoginViewModel
    abstract val homeViewModelFactory: HomeViewModel.Factory

    companion object
}
