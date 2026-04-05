package io.github.mitsuharu.showcase.feature.home

import org.koin.dsl.module

val featureHomeKoinModule = module {
    factory { (displayName: String, isGuest: Boolean) ->
        HomeViewModel(
            preferenceStorage = get(),
            displayName = displayName,
            isGuest = isGuest,
        )
    }
}
