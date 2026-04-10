package io.github.yarn44.kmp.showcase.shared

import io.github.yarn44.kmp.showcase.core.data.coreDataKoinModule
import io.github.yarn44.kmp.showcase.core.foundation.startKoinForIos
import io.github.yarn44.kmp.showcase.feature.home.HomeViewModel
import io.github.yarn44.kmp.showcase.feature.home.featureHomeKoinModule
import io.github.yarn44.kmp.showcase.feature.login.LoginViewModel
import io.github.yarn44.kmp.showcase.feature.login.featureLoginKoinModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

fun initKoin() {
    startKoinForIos(
        coreDataKoinModule,
        featureLoginKoinModule,
        featureHomeKoinModule,
    )
}

/**
 * Helper for resolving ViewModels from iOS.
 */
object ViewModelProvider : KoinComponent {
    fun loginViewModel(): LoginViewModel {
        val vm: LoginViewModel by inject()
        return vm
    }

    fun homeViewModel(displayName: String, isGuest: Boolean): HomeViewModel {
        val vm: HomeViewModel by inject { parametersOf(displayName, isGuest) }
        return vm
    }
}

fun getLoginViewModel(): LoginViewModel = ViewModelProvider.loginViewModel()

fun getHomeViewModel(displayName: String, isGuest: Boolean): HomeViewModel =
    ViewModelProvider.homeViewModel(displayName, isGuest)
