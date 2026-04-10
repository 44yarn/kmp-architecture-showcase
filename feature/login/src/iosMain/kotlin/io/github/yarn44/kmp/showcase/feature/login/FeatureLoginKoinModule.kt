package io.github.yarn44.kmp.showcase.feature.login

import io.github.yarn44.kmp.showcase.core.uikit.dialog.DialogPresenter
import io.github.yarn44.kmp.showcase.core.uikit.indicator.IndicatorState
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val featureLoginKoinModule = module {
    factory { IndicatorState() }
    factory { DialogPresenter() }
    factoryOf(::LoginViewModel)
}
