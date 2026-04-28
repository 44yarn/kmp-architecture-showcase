package io.github.yarn44.kmp.showcase.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import io.github.yarn44.kmp.showcase.core.foundation.navigation.screen
import io.github.yarn44.kmp.showcase.di.ShowcaseAppComponent
import io.github.yarn44.kmp.showcase.feature.home.HomeRoute
import io.github.yarn44.kmp.showcase.feature.home.HomeScreen
import io.github.yarn44.kmp.showcase.feature.info.InfoRoute
import io.github.yarn44.kmp.showcase.feature.info.InfoScreen
import io.github.yarn44.kmp.showcase.feature.login.LoginRoute
import io.github.yarn44.kmp.showcase.feature.login.LoginScreen

@Composable
fun ShowcaseNavGraph(
    appComponent: ShowcaseAppComponent,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute,
    ) {
        screen<LoginRoute> {
            LoginScreen(
                onNavigateToHome = { displayName, isGuest ->
                    navController.navigate(HomeRoute(displayName, isGuest)) {
                        popUpTo(LoginRoute) { inclusive = true }
                    }
                },
                onNavigateToInfo = {
                    navController.navigate(InfoRoute)
                },
                viewModel = viewModel { appComponent.loginViewModel },
            )
        }
        screen<HomeRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<HomeRoute>()
            HomeScreen(
                onNavigateToLogin = {
                    navController.navigate(LoginRoute) {
                        popUpTo(HomeRoute::class) { inclusive = true }
                    }
                },
                viewModel = viewModel(
                    key = "home_${route.displayName}_${route.isGuest}",
                ) {
                    appComponent.homeViewModelFactory.create(
                        displayName = route.displayName,
                        isGuest = route.isGuest,
                    )
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
