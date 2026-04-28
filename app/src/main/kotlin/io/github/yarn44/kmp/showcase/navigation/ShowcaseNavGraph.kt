package io.github.yarn44.kmp.showcase.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.yarn44.kmp.showcase.core.foundation.navigation.screen
import io.github.yarn44.kmp.showcase.di.ShowcaseAppGraph
import io.github.yarn44.kmp.showcase.feature.home.HomeRoute
import io.github.yarn44.kmp.showcase.feature.home.HomeScreen
import io.github.yarn44.kmp.showcase.feature.info.InfoRoute
import io.github.yarn44.kmp.showcase.feature.info.InfoScreen
import io.github.yarn44.kmp.showcase.feature.login.LoginRoute
import io.github.yarn44.kmp.showcase.feature.login.LoginScreen

/**
 * Single-Activity navigation host for the Showcase app.
 *
 * All three destinations (Login / Home / Info) live inside this one
 * `NavHost`; Info is pushed on top of Login rather than launched as a
 * separate Activity. Before the Single-Activity refactor an
 * `ActivityLauncher` interface routed the Info target through
 * `startActivity(Intent(...))`, which has been removed.
 *
 * ViewModels are retrieved via the direct accessor pattern on [ShowcaseAppGraph]
 * (`appGraph.loginViewModel`, `appGraph.homeViewModelFactory`) rather than
 * through a multibinding map, to work around Metro 0.10.4's inability to
 * resolve `@ContributesIntoMap` across Gradle module boundaries.
 */
@Composable
fun ShowcaseNavGraph(appGraph: ShowcaseAppGraph) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute,
    ) {
        screen<LoginRoute> {
            LoginScreen(
                viewModel = viewModel { appGraph.loginViewModel },
                onNavigateToHome = { displayName, isGuest ->
                    navController.navigate(HomeRoute(displayName, isGuest)) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                onNavigateToInfo = {
                    navController.navigate(InfoRoute)
                },
            )
        }
        screen<HomeRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<HomeRoute>()
            HomeScreen(
                viewModel = viewModel(
                    key = "${route.displayName}-${route.isGuest}",
                ) {
                    appGraph.homeViewModelFactory.create(
                        displayName = route.displayName,
                        isGuest = route.isGuest,
                    )
                },
                onNavigateToLogin = {
                    navController.navigate(LoginRoute) {
                        popUpTo(HomeRoute::class) { inclusive = true }
                    }
                },
            )
        }
        screen<InfoRoute> {
            InfoScreen(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
