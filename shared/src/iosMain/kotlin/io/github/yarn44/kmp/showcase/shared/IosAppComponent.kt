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
interface IosAppComponent {

    val loginViewModel: LoginViewModel
    val homeViewModelFactory: HomeViewModel.Factory
}

private var iosAppComponentInstance: IosAppComponent? = null

private fun requireComponent(): IosAppComponent =
    requireNotNull(iosAppComponentInstance) {
        "IosAppComponent not initialized. Call bootstrapIosAppComponent() first."
    }

fun bootstrapIosAppComponent() {
    if (iosAppComponentInstance == null) {
        iosAppComponentInstance = createGraph<IosAppComponent>()
    }
}

fun getLoginViewModel(): LoginViewModel =
    requireComponent().loginViewModel

fun getHomeViewModel(displayName: String, isGuest: Boolean): HomeViewModel =
    requireComponent().homeViewModelFactory.create(displayName, isGuest)
