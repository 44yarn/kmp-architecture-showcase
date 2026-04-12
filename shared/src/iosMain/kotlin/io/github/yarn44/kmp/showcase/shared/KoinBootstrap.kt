package io.github.yarn44.kmp.showcase.shared

import io.github.yarn44.kmp.showcase.core.data.coreDataKoinModule
import io.github.yarn44.kmp.showcase.core.foundation.CoreKoinBridgeIos
import io.github.yarn44.kmp.showcase.core.foundation.startKoinForIos
import io.github.yarn44.kmp.showcase.feature.home.HomeViewModel
import io.github.yarn44.kmp.showcase.feature.home.featureHomeKoinModule
import io.github.yarn44.kmp.showcase.feature.login.LoginViewModel
import io.github.yarn44.kmp.showcase.feature.login.featureLoginKoinModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

fun bootstrapKoin() {
    if (CoreKoinBridgeIos.isStarted()) return
    startKoinForIos(
        coreDataKoinModule,
        featureLoginKoinModule,
        featureHomeKoinModule,
    )
}

private object KoinBridge : KoinComponent

fun getLoginViewModel(): LoginViewModel {
    val vm: LoginViewModel by KoinBridge.inject()
    return vm
}

fun getHomeViewModel(displayName: String, isGuest: Boolean): HomeViewModel {
    val vm: HomeViewModel by KoinBridge.inject { parametersOf(displayName, isGuest) }
    return vm
}
