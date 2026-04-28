package io.github.yarn44.kmp.showcase.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.github.yarn44.kmp.showcase.core.foundation.coroutines.IoDispatcher
import io.github.yarn44.kmp.showcase.feature.home.HomeViewModel
import io.github.yarn44.kmp.showcase.feature.login.LoginViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import okio.Path.Companion.toPath

/**
 * Metro dependency graph for the Showcase Android app.
 *
 * Abstract accessor properties (`loginViewModel`, `homeViewModelFactory`) let
 * the Compose navigation layer retrieve ViewModels directly from the graph
 * via `viewModel { appGraph.loginViewModel }` / `viewModel { appGraph.homeViewModelFactory.create(...) }`.
 * This sidesteps the `@ContributesIntoMap` cross-module multibinding that
 * Metro 0.10.4 cannot handle reliably (the compiler plugin reaches contributor
 * declarations through `IR_EXTERNAL_DECLARATION_STUB` and fails with
 * `IllegalStateException: Parent of this declaration is not a class`).
 *
 * ## Why all `@Provides` live in this interface instead of in
 * `@ContributesTo` interfaces in other modules
 *
 * Same root cause: putting every provider directly on `ShowcaseAppGraph`
 * (which lives in the same module as the `@DependencyGraph` annotation)
 * sidesteps the cross-module stub handling entirely. This is strictly a
 * migration-era workaround — once Metro is upgraded to a version whose
 * compiler plugin supports cross-module contributors cleanly (and Kotlin is
 * upgraded to 2.4 to satisfy Metro 0.13.x), the providers can move back to
 * their natural owning modules (`core/foundation` for dispatchers,
 * `core/data` for DataStore).
 */
@DependencyGraph(scope = AppScope::class)
interface ShowcaseAppGraph {

    val loginViewModel: LoginViewModel
    val homeViewModelFactory: HomeViewModel.Factory

    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

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

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides applicationContext: Context,
        ): ShowcaseAppGraph
    }
}
