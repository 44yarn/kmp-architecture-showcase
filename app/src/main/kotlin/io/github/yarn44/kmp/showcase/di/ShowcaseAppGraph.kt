package io.github.yarn44.kmp.showcase.di

import android.content.Context
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.yarn44.kmp.showcase.feature.home.HomeViewModel
import io.github.yarn44.kmp.showcase.feature.login.LoginViewModel

@DependencyGraph(AppScope::class)
@SingleIn(AppScope::class)
interface ShowcaseAppGraph {

    val loginViewModel: LoginViewModel
    val homeViewModelFactory: HomeViewModel.Factory

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(@Provides applicationContext: Context): ShowcaseAppGraph
    }
}
