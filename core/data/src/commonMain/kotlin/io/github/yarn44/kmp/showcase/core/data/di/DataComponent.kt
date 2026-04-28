package io.github.yarn44.kmp.showcase.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.github.yarn44.kmp.showcase.core.data.preference.PreferenceStorage
import io.github.yarn44.kmp.showcase.core.foundation.coroutines.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@ContributesTo(AppScope::class)
interface DataComponent {

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @SingleIn(AppScope::class)
    fun providePreferenceStorage(
        dataStore: DataStore<Preferences>,
    ): PreferenceStorage =
        PreferenceStorage(dataStore)
}
