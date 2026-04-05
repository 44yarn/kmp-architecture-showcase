package io.github.mitsuharu.showcase.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.mitsuharu.showcase.core.foundation.ActivityLauncher
import io.github.mitsuharu.showcase.core.foundation.navigation.screen
import io.github.mitsuharu.showcase.feature.home.HomeRoute
import io.github.mitsuharu.showcase.feature.home.HomeScreen
import io.github.mitsuharu.showcase.feature.login.LoginRoute
import io.github.mitsuharu.showcase.feature.login.LoginScreen

@Composable
fun ShowcaseNavGraph(
    activityLauncher: ActivityLauncher,
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
                onLaunchActivity = {
                    activityLauncher.launch(ActivityLauncher.Target.Info)
                },
            )
        }
        screen<HomeRoute> {
            HomeScreen(
                onNavigateToLogin = {
                    navController.navigate(LoginRoute) {
                        popUpTo(HomeRoute::class) { inclusive = true }
                    }
                },
            )
        }
    }
}
