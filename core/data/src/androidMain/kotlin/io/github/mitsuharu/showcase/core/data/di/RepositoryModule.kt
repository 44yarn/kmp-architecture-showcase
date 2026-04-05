package io.github.mitsuharu.showcase.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.mitsuharu.showcase.core.data.auth.AndroidAuthRepositoryImpl
import io.github.mitsuharu.showcase.core.data.auth.AuthRepository
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorage
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorageImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AndroidAuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPreferenceStorage(impl: PreferenceStorageImpl): PreferenceStorage
}
