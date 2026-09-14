package io.github.yarn44.kmp.showcase.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import okio.Path.Companion.toPath

@ContributesTo(AppScope::class)
interface AndroidDataStoreProviders {

    @Provides
    @SingleIn(AppScope::class)
    fun provideDataStore(context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                context.filesDir
                    .resolve("datastore/showcase_prefs.preferences_pb")
                    .absolutePath
                    .toPath()
            },
        )
}
