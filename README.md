# KMP Architecture Showcase

[Japanese](README.ja.md)

A Kotlin Multiplatform (KMP) sample application demonstrating cross-platform architecture patterns.
The same Login / Home / Info screens are implemented with Jetpack Compose (Android) and SwiftUI (iOS),
sharing ViewModels, repositories, and design tokens through a common module.

Based on [android-architecture-showcase](https://github.com/44yarn/android-architecture-showcase).

## Screen Flow

```
Login --- Success ------------> Home ("Welcome, {name}!" Snackbar)
  |                                PreferenceStorage demo
  |
  +--- Failure -> ErrorDialog (DialogPresenter in commonMain)
  |         +- Cancel -> dismiss
  |         +- Guest Login -> Home ("Guest mode" Snackbar)
  |
  +--- Information -> Info screen
```

## Showcased Patterns

| Screen | Module | Patterns |
|--------|--------|----------|
| Login | feature:login | DialogPresenter (commonMain), IndicatorState, Effect (navigation) |
| Home | feature:home | SnackbarPresenter, PreferenceStorage, BackHandler (Android) |
| Info | feature:info | Static content display |

### UI Feedback Patterns

| Pattern | Use Case | Mechanism |
|---------|----------|-----------|
| Effect | Fire-and-forget (navigation) | `Channel<Effect>` + `receiveAsFlow()` |
| Dialog | Awaiting user response | `DialogPresenter` + `suspendCancellableCoroutine` (commonMain) |
| Snackbar | Immediate display | `SnackbarPresenter.show()` direct call |

### Cross-Platform Design System

| Layer | Location | Description |
|-------|----------|-------------|
| Design Token Values | `commonMain` | `AppColorValues`, `AppTypographyValues`, `AppSpacingValues`, `AppCornerRadiusValues` (platform-agnostic raw values) |
| Compose Wrappers | `commonMain` | `AppColorToken`, `AppTypography`, `AppSpacing`, `CornerRadius` |
| Material3 Mapping | `androidMain` | `AppColorScheme`, `AppThemeProvider` |
| SwiftUI Wrappers | `iosApp/Theme/` | `AppColors`, `AppFonts`, `AppSpacings`, `AppCornerRadii` |

### Other Design Patterns

- **KMP ViewModel** — Common ViewModel in `commonMain`, `@HiltViewModel` wrapper in `androidMain`
- **SKIE** — Kotlin `StateFlow` / `Flow` automatically bridged to Swift `AsyncSequence`
- **Actions class** — Callbacks aggregated into a data class
- **Convention Plugin** — Shared build configuration via gradle-conventions
- **PreferenceKey / PreferenceStorage** — Type-safe wrapper over Preferences DataStore (KMP), with the value type carried by the key
- **AdaptiveString / AdaptiveImage** — Unify localized resources with literal strings / remote URLs behind a single consumer-facing type (see below)
- **DI boundary** — Repositories and use-case classes live in `commonMain`. Platform-specific APIs (DataStore file paths, `Context`, `NSFileManager`, …) are referenced only inside DI modules (Hilt `@Provides` / Koin `module`) and injected as dependencies. No thin Hilt wrapper class just to attach `@Inject` — common classes are instantiated directly from `@Provides`.
- **DispatcherProvider** — Repositories receive a `DispatcherProvider` interface (in `core/foundation/commonMain`) instead of referencing `Dispatchers.IO` / `.Default` / `.Main` directly. Production binding is `DefaultDispatcherProvider`; tests substitute a `TestDispatcherProvider` backed by `StandardTestDispatcher`, which lets `runTest` virtualize production `delay()` calls (see `core/data/commonTest/.../AuthRepositoryTest.kt`). The interface approach avoids JVM-only `@Qualifier` annotations and works uniformly under both Hilt and Koin.
- **Stateless content split** — Each Android screen pairs a stateful `XxxScreen` (collects ViewModel state, wires effects) with a stateless `XxxContent` that only takes `uiState` + `actions`. The latter is what `@Preview` renders, so previews never touch DI or coroutines.
- **Lifecycle-aware effect collection** — One-shot effect channels are collected via `Flow<T>.CollectAsEffect` (in `core/foundation`), which wraps `repeatOnLifecycle(STARTED)` so effects are not delivered while the screen is in the background.
- **DI** — Hilt (Android) + Koin (iOS)

### Adaptive types: mixing resources and runtime values

This project ships two related types in `core/ui-kit` — `AdaptiveString`
and `AdaptiveImage` — that share the same design philosophy. Each is an
**encapsulated single class** that unifies a localized Compose
Multiplatform resource with a runtime value (a literal string or a
remote URL) behind a single consumer-facing type.

**Why this matters.** Real-world projects routinely need to mix two
sources of text or image assets in the same UI field:

- **Known states or local assets** → mapped to a **localized resource**
  by the UI layer
  (e.g. `is AuthException -> AdaptiveString(Res.string.login_invalid_credentials)`)
- **Server-provided text or remote images** → carried as a **literal or
  URL** (e.g. `AdaptiveString("An unexpected error occurred.")`, or
  `AdaptiveImage("https://example.com/avatar.png")`)

Without a unified type, every caller that builds a dialog, a snackbar,
or an image slot would have to branch between `String` and
`StringResource`, or between URL and `DrawableResource`. These types
hide the distinction behind overloaded constructors and a single
resolve path, so ViewModels can emit either form and the UI layer just
renders it.

Both types use the same encapsulation pattern: a **private primary
constructor** holds the internal `val` fields, and a handful of
**secondary constructors** expose only valid combinations. Clients
cannot construct an invalid hybrid shape.

#### `AdaptiveString`

Holds either a plain literal `String` or a localized `StringResource`
(plus optional format arguments). Exposes `@Composable val value: String`
for Android Compose, and a `suspend fun resolve(): String` extension in
`iosMain` for SwiftUI — SKIE bridges it to a Swift `async throws` call.

**Showcase location.** See `LoginViewModel.showLoginErrorDialog` in
`feature/login`. It maps `AuthException` to a localized resource and any
other throwable to a literal fallback, feeding both into the same
`DialogUiState.message` field.

**Design principle: `Exception.message` is for logging, not UI.** The
sample-only `AuthException` in `core/data` carries a diagnostic message
for logs only. Mapping errors to user-facing text is the responsibility
of the UI layer (i.e. the ViewModel), which chooses an appropriate
`AdaptiveString` based on the exception's type — not on its `message`.

#### `AdaptiveImage`

Holds either a remote image URL or a local `DrawableResource`, along
with an `ImageType` that describes how the image should be displayed:

- `ImageType.Icon` — small, square, icon-style
- `ImageType.FillMaxWidth(contentScale, aspectRatio)` — stretched to the
  container's full width with a fixed aspect ratio

Rendering is intentionally left to the caller. A Composable renderer
can branch on `type` and pick between `AsyncImage(url)` and
`painterResource(resource)` depending on which field is populated.

`AdaptiveImage` is not yet consumed by any screen in this showcase; it
lives here as a ready-to-use pattern for the next time an avatar,
thumbnail, or header image needs to blend remote and local sources.

#### Resource ownership

String and drawable resources live in each feature's
`src/commonMain/composeResources/` tree (under `values/strings.xml` for
strings, `drawable/` for images), next to the feature that uses them.
`core/ui-kit` hosts only the `AdaptiveString` / `AdaptiveImage` types
themselves — no resource files.

## Module Structure

```
gradle-conventions       Convention Plugins (shared build configuration)
app                      Android app entry point, NavGraph, Hilt setup
shared                   Umbrella framework (ShowcaseKit) for iOS
+-- core
|   +-- foundation       KmpViewModel, Result extensions
|   +-- ui-kit           DialogPresenter, SnackbarPresenter, IndicatorState, AppTheme, AdaptiveString, AdaptiveImage
|   +-- data             AuthRepository, PreferenceStorage (DataStore KMP)
+-- feature
|   +-- login            Login screen
|   +-- home             Home screen
|   +-- info             Info screen
iosApp                   iOS app (SwiftUI + XcodeGen)
```

Dependency direction: `app/iosApp -> feature -> core` (unidirectional).

## Convention Plugins (gradle-conventions)

| Plugin | Role |
|--------|------|
| showcase.convention.app | Application module setup |
| showcase.convention.kmp-feature | KMP feature module (Compose + SKIE) |
| showcase.convention.kmp-module | Base plugin for KMP core / library modules |
| showcase.convention.kmp-sqldelight | Adds SQLDelight to a KMP module |
| showcase.primitive.hilt | Hilt DI + KSP |
| showcase.primitive.spotless | Code formatting |
| showcase.primitive.detekt | Static analysis |

## Tech Stack

| Category | Library |
|----------|---------|
| Language | Kotlin 2.3 / Swift 5.9 |
| UI | Jetpack Compose (Android) / SwiftUI (iOS) |
| DI | Hilt (Android) / Koin (iOS) |
| Navigation | Navigation Compose (Android) / NavigationStack (iOS) |
| Async | Kotlin Coroutines + Flow / SKIE AsyncSequence |
| Storage | Preferences DataStore (KMP) |
| Build | AGP 8.13, KSP, Convention Plugins, XcodeGen |
| Code Quality | Spotless, detekt, SwiftFormat, SwiftLint |

## Requirements

- JDK 21
- Android: minSdk 31 / compileSdk 36 / targetSdk 36
- iOS: 17.0+
- Xcode 16+
- [Mint](https://github.com/yonaskolb/Mint) (for SwiftFormat / SwiftLint)
- [XcodeGen](https://github.com/yonaskolb/XcodeGen) (for Xcode project generation)

## Build

```bash
# Android debug build
./gradlew assembleDebug

# Unit tests
./gradlew testDebugUnitTest

# Code formatting (Kotlin)
./gradlew spotlessApply

# Static analysis
./gradlew detekt

# Swift formatting
./script/format-swift.sh

# iOS framework
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64

# Full preflight pipeline (runs all of the above + iOS build)
./script/preflight.sh
```

### iOS + Compose Multiplatform Resources

Compose Multiplatform Resources (`StringResource`, `DrawableResource`,
etc.) are designed with Compose Multiplatform UI in mind. When the iOS
side uses SwiftUI instead of Compose UI — as this showcase does — the
resource `.cvr` files are compiled into the Kotlin framework but are
**not automatically copied** into the iOS app bundle, producing a runtime
`MissingResourceException` the first time any `StringResource` is
resolved from Swift.

This project works around the gap with a small Run Script Build Phase
in Xcode (`script/sync-compose-resources.sh`, wired up via
`iosApp/project.yml`). The script runs the
`assemble<Target>MainResources` Gradle task for every feature module
that owns `composeResources/`, then rsyncs the result into the built
`.app` bundle at the layout expected by the Compose Resources runtime.

If you add a new feature module with its own `composeResources/`
directory, append its Gradle path to the `MODULES` array in
`script/sync-compose-resources.sh`.

## License

    Copyright 2026 44yarn

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
