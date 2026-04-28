# Metro DI Migration — PoC Report

[Japanese / 日本語](metro-migration-ja.md)

## Overview

This branch migrates the project's DI from **Hilt (Android) + Koin (iOS)** dual setup to **Metro 0.10.4** (KMP unified, compile-time graph validation).

Metro is a Kotlin compiler plugin for dependency injection by [Zac Sweers](https://github.com/ZacSweers/metro), inspired by Dagger, Anvil, and kotlin-inject.

### Why not merged?

Metro is **pre-1.0** (0.10.4) and has known limitations. This PR is kept open as a reference implementation — a "what KMP unified DI looks like" showcase — rather than the recommended production setup.

**Merge conditions:**
- Kotlin 2.4 stable + Compose Multiplatform support for Kotlin 2.4
- Metro 0.13.x+ (or 1.0) with cross-module `@ContributesTo` fix
- These three conditions aligning would make Metro production-ready for this project

---

## Migration Stages

| Stage | Description | Commit |
|---|---|---|
| 0 | Metro plugin infrastructure | `chore(gradle): add Metro DI plugin infrastructure` |
| 1 | Replace `DispatcherProvider` with `@IoDispatcher` | `refactor(core): replace DispatcherProvider with Metro @IoDispatcher` |
| 2 | Migrate Login to Metro + Single-Activity | `build: downgrade Metro to 0.10.4...` + `refactor: migrate login to Metro DI...` |
| 3 | Migrate Home with assisted factory | `refactor: migrate home to Metro assisted DI` |
| 4 | Replace Koin with Metro on iOS | `refactor(ios): migrate from Koin to Metro via IosAppGraph` |
| 4-fix | Fix cross-module multibinding crash | `fix: use direct graph accessors instead of cross-module multibinding` |
| 5 | Remove Hilt + Koin completely | `chore: remove Hilt, Koin, and metrox-viewmodel dependencies` |

---

## Key Findings

### 1. Kotlin version compatibility

Metro 0.13.x requires **Kotlin 2.4.0-Beta1+** (IR API mismatch with 2.3.x). Since Compose Multiplatform 1.10.3 does not yet support Kotlin 2.4, we downgraded to **Metro 0.10.4** which is compatible with **Kotlin 2.3.10**.

### 2. Cross-module `@ContributesTo` / `@ContributesIntoMap` does not work

Metro 0.10.4's compiler plugin cannot resolve `@ContributesTo` or `@ContributesIntoMap` contributions declared in a different Gradle module from the `@DependencyGraph`. The plugin accesses these declarations through `IR_EXTERNAL_DECLARATION_STUB` and fails with `IllegalStateException`.

**Workaround:** All `@Provides` functions are inlined directly into the graph interface (`ShowcaseAppGraph` on Android, `IosAppGraph` on iOS). ViewModels are exposed through direct abstract accessors (`val loginViewModel: LoginViewModel`) rather than multibinding maps.

### 3. `@Inject constructor` supports default arguments

Unlike Dagger/Hilt, Metro allows default argument values in `@Inject` constructors. This lets us write `LoginViewModel(..., indicatorState: IndicatorState = IndicatorState())` without needing `@Provides` or `@AssistedInject` for the defaulted parameters.

### 4. `@AssistedFactory` works in commonMain

Metro's `@AssistedInject` + `@AssistedFactory` annotations are processed by the compiler plugin on all KMP targets. `HomeViewModel.Factory` is declared in `commonMain` and works on both Android and iOS.

### 5. `metrox-viewmodel-compose` was ultimately not needed

Due to the cross-module limitation (#2), the `MetroViewModelFactory` multibinding approach didn't work. We switched to direct graph accessors + standard `viewModel { }` composable, making `metrox-viewmodel-compose` unnecessary.

### 6. iOS Swift interop

- Kotlin function names starting with `init` are renamed to `doInit...` in Swift. Use verbs like `bootstrap` instead.
- `Dispatchers.IO` on Kotlin/Native is `internal`; use the `kotlinx.coroutines.IO` extension import.
- The iOS graph uses `createGraph<IosAppGraph>()` (no factory needed since no runtime parameters).

### 7. Side effects of migration

- **Single-Activity architecture**: `InfoActivity` and `ActivityLauncher` were removed; all screens now live in one `NavHost`.
- **`KmpViewModel` extends `androidx.lifecycle.ViewModel`**: The custom base class now properly integrates with the Android ViewModel lifecycle.
- **Android `@HiltViewModel` wrappers eliminated**: `AndroidLoginViewModel` (52 lines) and `AndroidHomeViewModel` (52 lines) were deleted.

---

## Architecture After Migration

### Android

```
ShowcaseApplication
  └── appGraph: ShowcaseAppGraph (lazy, @DependencyGraph)
        ├── val loginViewModel: LoginViewModel (@Inject)
        ├── val homeViewModelFactory: HomeViewModel.Factory (@AssistedFactory)
        ├── @Provides fun provideIoDispatcher()
        └── @Provides fun provideDataStore(context)

MainActivity
  └── setContentWithTheme {
        ShowcaseNavGraph(appGraph = appGraph)
          ├── LoginScreen(viewModel = viewModel { appGraph.loginViewModel })
          ├── HomeScreen(viewModel = viewModel { appGraph.homeViewModelFactory.create(...) })
          └── InfoScreen(onBack = ...)
      }
```

### iOS

```
IosAppGraph (@DependencyGraph, singleton)
  ├── val loginViewModel: LoginViewModel
  ├── val homeViewModelFactory: HomeViewModel.Factory
  ├── @Provides fun provideIoDispatcher()
  └── @Provides fun provideDataStore()

Top-level helpers (Swift-facing):
  ├── bootstrapIosAppGraph()    → ShowcaseApp.init
  ├── getLoginViewModel()       → LoginView
  └── getHomeViewModel(...)     → HomeView
```

---

## File Changes Summary

### Deleted

| File | Reason |
|---|---|
| `AndroidLoginViewModel.kt` | Replaced by Metro `@Inject` on `LoginViewModel` |
| `AndroidHomeViewModel.kt` | Replaced by Metro `@AssistedInject` on `HomeViewModel` |
| `DispatcherProvider.kt` | Replaced by `@IoDispatcher` qualifier in commonMain |
| `TestDispatcherProvider.kt` | Tests pass `StandardTestDispatcher` directly |
| `ActivityLauncher.kt` | Single-Activity refactor |
| `ActivityLauncherImpl.kt` | Single-Activity refactor |
| `ActivityLauncherModule.kt` | Single-Activity + Hilt removal |
| `InfoActivity.kt` | Single-Activity refactor |
| `PreferenceModule.kt` (Hilt) | Metro provides DataStore via graph |
| `RepositoryModule.kt` (Hilt) | Metro auto-resolves via `@Inject` |
| `hilt.gradle.kts` | Convention plugin replaced by `metro.gradle.kts` |
| `KoinBootstrap.kt` | Replaced by `IosAppGraph.kt` |
| `CoreKoinBridgeIos.kt` | Replaced by `IosAppGraph.kt` |
| `CoreDataKoinModule.kt` | Replaced by `IosAppGraph.kt` |
| `FeatureLoginKoinModule.kt` | Replaced by `IosAppGraph.kt` |
| `FeatureHomeKoinModule.kt` | Replaced by `IosAppGraph.kt` |
| `ShowcaseViewModelFactory.kt` | Direct accessor pattern replaced multibinding |
| `AndroidDataStoreHolder.kt` | Inlined into graph provider |

### Created

| File | Purpose |
|---|---|
| `ShowcaseAppGraph.kt` | Android Metro `@DependencyGraph` |
| `IosAppGraph.kt` | iOS Metro `@DependencyGraph` + Swift helpers |
| `IoDispatcher.kt` | Metro `@Qualifier` in commonMain |
| `InfoRoute.kt` | Compose Navigation route for Single-Activity |
| `metro.gradle.kts` | Convention plugin for Metro |

---

## iOS Build Notes

When building the iOS app after Kotlin-side changes, the following steps are required:

### 1. Rebuild the KMP framework before Xcode build

```bash
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```

Xcode does **not** automatically rebuild the Kotlin framework. If you skip this step, Xcode will compile against stale `.framework` output and you may see `No such module 'ShowcaseKit'` or unresolved symbol errors.

### 2. Specify a concrete simulator for xcodebuild (Apple Silicon)

```bash
# List available simulators
xcrun simctl list devices available | grep iPhone

# Build with a specific device (arm64 only)
xcodebuild -project iosApp/iosApp.xcodeproj \
  -scheme iosApp -configuration Debug \
  -destination 'platform=iOS Simulator,name=iPhone 16' \
  ONLY_ACTIVE_ARCH=YES build
```

Using `-destination 'generic/platform=iOS Simulator'` on Apple Silicon attempts to build **both arm64 and x86_64** slices. Since the KMP framework is only built for `iosSimulatorArm64`, the x86_64 slice fails with `module file is incompatible with this Swift compiler`. Always specify a concrete simulator device or add `ONLY_ACTIVE_ARCH=YES`.

### 3. Clean Xcode caches when switching branches

After rebasing or switching between branches that modify the KMP framework API (e.g. renaming Kotlin top-level functions), Xcode's DerivedData may become stale:

```bash
# Option A: Clean Build Folder in Xcode (Cmd+Shift+K)
# Option B: Delete DerivedData
rm -rf ~/Library/Developer/Xcode/DerivedData/iosApp-*
```

### 4. Git worktrees and Xcode

When using `git worktree`, open the Xcode project from the **worktree directory**, not the main repo. Each worktree has its own `shared/build/` output, and Xcode resolves the framework path relative to the project location.

---

## References

- [Metro GitHub](https://github.com/ZacSweers/metro)
- [DroidKaigi conference-app-2025](https://github.com/DroidKaigi/conference-app-2025) — Metro adoption reference
- [Introducing Metro (Zac Sweers blog)](https://www.zacsweers.dev/introducing-metro/)
