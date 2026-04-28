package io.github.yarn44.kmp.showcase.shared

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import io.github.yarn44.kmp.showcase.core.data.preference.PreferenceStorage
import io.github.yarn44.kmp.showcase.core.foundation.coroutines.IoDispatcher
import io.github.yarn44.kmp.showcase.core.ui.dialog.DialogPresenter
import io.github.yarn44.kmp.showcase.core.ui.indicator.IndicatorState
import io.github.yarn44.kmp.showcase.core.ui.snackbar.SnackbarPresenter
import io.github.yarn44.kmp.showcase.feature.home.HomeViewModel
import io.github.yarn44.kmp.showcase.feature.login.LoginViewModel
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

/**
 * iOS DI graph.
 *
 * Ideally this would use `@MergeComponent` to auto-merge `@ContributesTo`
 * contributions from other modules. However, cross-module KSP contribution
 * discovery on iOS targets requires further investigation. For now, all
 * providers are inlined here (same pattern as the Metro PoC fallback).
 */
@Component
@SingleIn(AppScope::class)
abstract class IosAppComponent {

    abstract val loginViewModel: LoginViewModel
    abstract val homeViewModelFactory: HomeViewModel.Factory

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @SingleIn(AppScope::class)
    fun provideDataStore(): DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { providePreferencesPath() },
        )

    @Provides
    @SingleIn(AppScope::class)
    fun providePreferenceStorage(dataStore: DataStore<Preferences>): PreferenceStorage =
        PreferenceStorage(dataStore)

    @Provides
    fun provideIndicatorState(): IndicatorState = IndicatorState()

    @Provides
    fun provideDialogPresenter(): DialogPresenter = DialogPresenter()

    @Provides
    fun provideSnackbarPresenter(): SnackbarPresenter = SnackbarPresenter()

    companion object
}

@OptIn(ExperimentalForeignApi::class)
private fun providePreferencesPath(): Path {
    val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val basePath = requireNotNull(documentDirectory?.path) {
        "Unable to resolve NSDocumentDirectory for DataStore"
    }
    return "$basePath/showcase_prefs.preferences_pb".toPath()
}

internal expect fun createIosAppComponent(): IosAppComponent

private var iosAppComponentInstance: IosAppComponent? = null

private fun requireComponent(): IosAppComponent =
    requireNotNull(iosAppComponentInstance) {
        "IosAppComponent not initialized. Call bootstrapIosAppComponent() first."
    }

fun bootstrapIosAppComponent() {
    if (iosAppComponentInstance == null) {
        iosAppComponentInstance = createIosAppComponent()
    }
}

fun getLoginViewModel(): LoginViewModel =
    requireComponent().loginViewModel

fun getHomeViewModel(displayName: String, isGuest: Boolean): HomeViewModel =
    requireComponent().homeViewModelFactory.create(displayName, isGuest)
