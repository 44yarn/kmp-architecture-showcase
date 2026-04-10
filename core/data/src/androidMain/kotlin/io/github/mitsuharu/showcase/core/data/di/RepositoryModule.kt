package io.github.mitsuharu.showcase.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.mitsuharu.showcase.core.data.auth.AuthRepository
import io.github.mitsuharu.showcase.core.data.auth.AuthRepositoryImpl
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl()

    @Provides
    @Singleton
    fun providePreferenceStorage(
        dataStore: DataStore<Preferences>,
    ): PreferenceStorage = PreferenceStorage(dataStore)
}
