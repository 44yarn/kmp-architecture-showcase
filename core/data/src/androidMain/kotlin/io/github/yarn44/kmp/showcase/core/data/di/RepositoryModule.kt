package io.github.yarn44.kmp.showcase.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.yarn44.kmp.showcase.core.data.auth.AuthRepository
import io.github.yarn44.kmp.showcase.core.foundation.coroutines.DefaultDispatcherProvider
import io.github.yarn44.kmp.showcase.core.foundation.coroutines.DispatcherProvider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider = DefaultDispatcherProvider()

    @Provides
    @Singleton
    fun provideAuthRepository(
        dispatchers: DispatcherProvider,
    ): AuthRepository = AuthRepository(dispatchers)
}
