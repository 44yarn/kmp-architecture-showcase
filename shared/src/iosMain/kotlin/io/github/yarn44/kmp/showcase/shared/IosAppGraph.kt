package io.github.yarn44.kmp.showcase.shared

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraph
import io.github.yarn44.kmp.showcase.feature.home.HomeViewModel
import io.github.yarn44.kmp.showcase.feature.login.LoginViewModel

/**
 * iOS DI graph.
 *
 * Every `@Provides` lives in the module that owns it and is pulled in here via
 * `@ContributesTo(AppScope::class)` aggregation, exactly like the Android graph.
 */
@DependencyGraph(AppScope::class)
@SingleIn(AppScope::class)
interface IosAppGraph {

    val loginViewModel: LoginViewModel
    val homeViewModelFactory: HomeViewModel.Factory
}

private var iosAppGraphInstance: IosAppGraph? = null

private fun requireGraph(): IosAppGraph =
    requireNotNull(iosAppGraphInstance) {
        "IosAppGraph not initialized. Call bootstrapIosAppGraph() first."
    }

fun bootstrapIosAppGraph() {
    if (iosAppGraphInstance == null) {
        iosAppGraphInstance = createGraph<IosAppGraph>()
    }
}

fun getLoginViewModel(): LoginViewModel =
    requireGraph().loginViewModel

fun getHomeViewModel(displayName: String, isGuest: Boolean): HomeViewModel =
    requireGraph().homeViewModelFactory.create(displayName, isGuest)
