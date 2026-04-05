package io.github.mitsuharu.showcase.core.data

import io.github.mitsuharu.showcase.core.data.auth.AuthRepository
import io.github.mitsuharu.showcase.core.data.auth.AuthRepositoryImpl
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorage
import io.github.mitsuharu.showcase.core.data.preference.PreferenceStorageIos
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataKoinModule = module {
    single<CoroutineDispatcher>(named("dispatcherIo")) { Dispatchers.Default }
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    singleOf(::PreferenceStorageIos) bind PreferenceStorage::class
}
