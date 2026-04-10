package io.github.yarn44.kmp.showcase.feature.home

import io.github.yarn44.kmp.showcase.core.uikit.snackbar.SnackbarPresenter
import org.koin.dsl.module

val featureHomeKoinModule = module {
    factory { (displayName: String, isGuest: Boolean) ->
        HomeViewModel(
            preferenceStorage = get(),
            displayName = displayName,
            isGuest = isGuest,
            snackbarPresenter = SnackbarPresenter(),
        )
    }
}
