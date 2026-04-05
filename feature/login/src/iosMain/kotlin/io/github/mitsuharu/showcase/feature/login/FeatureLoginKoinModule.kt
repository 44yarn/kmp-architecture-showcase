package io.github.mitsuharu.showcase.feature.login

import io.github.mitsuharu.showcase.core.uikit.indicator.IndicatorState
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val featureLoginKoinModule = module {
    factory { IndicatorState() }
    factoryOf(::LoginViewModel)
}
