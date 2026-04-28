package io.github.yarn44.kmp.showcase.shared

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.createGraph
import io.github.yarn44.kmp.showcase.core.foundation.coroutines.IoDispatcher
import io.github.yarn44.kmp.showcase.feature.home.HomeViewModel
import io.github.yarn44.kmp.showcase.feature.login.LoginViewModel
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

/**
 * Metro dependency graph for the iOS Showcase app.
 *
 * Mirrors [io.github.yarn44.kmp.showcase.di.ShowcaseAppGraph] on Android.
 * The iOS graph exposes view models through plain abstract accessors
 * ([loginViewModel], [homeViewModelFactory]), and Swift reaches them via
 * the [initIosAppGraph] / [getLoginViewModel] / [getHomeViewModel]
 * top-level helpers below — preserving the same flat API surface the
 * previous Koin-based `KoinBootstrap.kt` exposed so existing SwiftUI call
 * sites only need to swap the generated `Kt` class name.
 *
 * Every `@Provides` lives inline because Metro 0.10.4 trips over
 * cross-module `@ContributesTo` contributors with
 * `IR_EXTERNAL_DECLARATION_STUB` errors (see Metro issue #460). This is
 * the same workaround applied on the Android side in `ShowcaseAppGraph`
 * and will be revisited once Metro ships a compiler plugin that handles
 * cross-module graph assembly correctly.
 */
@DependencyGraph(scope = AppScope::class)
interface IosAppGraph {

    /**
     * `LoginViewModel` is resolved by Metro through its `@Inject`
     * constructor (Stage 2). Each access returns a new instance because
     * the VM is not annotated `@SingleIn(AppScope::class)` — SwiftUI's
     * `LoginView` holds the result in a `@State` property so one instance
     * is kept per screen lifetime.
     */
    val loginViewModel: LoginViewModel

    /**
     * `HomeViewModel` uses Metro's assisted injection (Stage 3), so the
     * graph exposes the generated factory rather than the view model
     * itself. Swift calls [getHomeViewModel] which forwards `displayName`
     * / `isGuest` (decoded from `AppRoute.home(...)`) to `create(...)`.
     */
    val homeViewModelFactory: HomeViewModel.Factory

    /**
     * `Dispatchers.IO` is only available in commonMain via the
     * `kotlinx.coroutines.IO` extension — the `Dispatchers.IO` member on
     * Kotlin/Native is `internal`. On Kotlin/Native the extension
     * ultimately delegates to `Dispatchers.Default`, which is the right
     * thing to do for our one IO-bound consumer (`AuthRepository`'s
     * simulated login delay).
     */
    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Single `DataStore<Preferences>` for the whole app, backed by a file
     * under `NSDocumentDirectory`. `@SingleIn(AppScope::class)` mirrors
     * the Android-side provider and is required because
     * `androidx.datastore` explicitly forbids multiple instances pointing
     * at the same path.
     */
    @Provides
    @SingleIn(AppScope::class)
    fun provideDataStore(): DataStore<Preferences> =
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { providePreferencesPath() },
        )
}

/**
 * Resolves the on-device file path for the DataStore preferences file
 * using Apple Foundation APIs. Kept as a file-level helper instead of a
 * method on [IosAppGraph] because Metro treats every abstract graph
 * method as a provider lookup and a non-`@Provides` helper method on the
 * interface would confuse the compiler plugin.
 */
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

// ---------------------------------------------------------------------------
// Swift-facing helpers — kept as top-level functions so the Kotlin/Native
// compiler emits them inside `IosAppGraphKt` and SwiftUI can call them as
// `IosAppGraphKt.initIosAppGraph()` / `IosAppGraphKt.getLoginViewModel()`
// etc., matching the old Koin-era call sites except for the `Kt` class
// prefix. The graph itself is stored in a lazily-initialised singleton.
// ---------------------------------------------------------------------------

private var iosAppGraphInstance: IosAppGraph? = null

/**
 * Creates the [IosAppGraph] instance on first call and caches it for all
 * subsequent graph lookups. Must be invoked from `ShowcaseApp.init` before
 * any SwiftUI screen tries to obtain a view model.
 *
 * Named `bootstrapIosAppGraph` rather than `initIosAppGraph` because
 * Kotlin/Native prefixes methods starting with `init` with `do` when
 * exporting them to Objective-C (Swift's `init` is a reserved keyword),
 * which would force SwiftUI call sites into the awkward
 * `IosAppGraphKt.doInitIosAppGraph()` spelling. Choosing a neutral verb
 * keeps the Swift API clean (`IosAppGraphKt.bootstrapIosAppGraph()`).
 *
 * Subsequent calls are no-ops, which makes this safe to call from
 * `@main`-like init paths that might run more than once in Preview
 * contexts.
 */
fun bootstrapIosAppGraph() {
    if (iosAppGraphInstance == null) {
        iosAppGraphInstance = createGraph<IosAppGraph>()
    }
}

/**
 * Returns a fresh [LoginViewModel]. SwiftUI's `LoginView` stores the
 * result in a `@State` property, so only one instance exists per screen
 * lifetime.
 */
fun getLoginViewModel(): LoginViewModel =
    requireGraph().loginViewModel

/**
 * Returns a [HomeViewModel] seeded with the navigation-supplied
 * [displayName] and [isGuest] values. The values originate from
 * `AppRoute.home(displayName:isGuest:)` on the Swift side and flow
 * through `HomeViewModel.Factory.create(...)` without touching
 * `SavedStateHandle` — that API is Android/Compose Navigation specific.
 */
fun getHomeViewModel(displayName: String, isGuest: Boolean): HomeViewModel =
    requireGraph().homeViewModelFactory.create(displayName = displayName, isGuest = isGuest)

private fun requireGraph(): IosAppGraph =
    requireNotNull(iosAppGraphInstance) {
        "IosAppGraph has not been initialised. Call bootstrapIosAppGraph() from ShowcaseApp.init before resolving view models."
    }
