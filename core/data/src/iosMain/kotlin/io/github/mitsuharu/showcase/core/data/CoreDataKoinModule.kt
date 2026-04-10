package io.github.mitsuharu.showcase.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import io.github.mitsuharu.showcase.core.data.auth.AuthRepository
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorage
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

val coreDataKoinModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { providePreferencesPath() },
        )
    }
    single { AuthRepository() }
    single { PreferenceStorage(get()) }
}

@OptIn(ExperimentalForeignApi::class)
private fun providePreferencesPath(): okio.Path {
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
