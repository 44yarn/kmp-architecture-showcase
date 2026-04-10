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
- **AdaptiveString** — Unifies localized resources and literal strings behind a single type (see below)
- **DI** — Hilt (Android) + Koin (iOS)

### AdaptiveString: mixing localized resources and literal strings

`AdaptiveString` is an encapsulated single class that can hold either a
`StringResource` (Compose Multiplatform Resources) or a plain literal `String`.
Consumers such as `DialogUiState` treat it as a single type and never branch
on which kind is stored inside.

**Why it matters.** Real-world projects routinely need to mix two sources of
text in the same UI field:

- **Known error types** → mapped to a **localized resource** by the UI layer
  (e.g. `is AuthException -> AdaptiveString(Res.string.login_invalid_credentials)`)
- **Unknown errors or server-provided text** → carried as a **literal**
  (e.g. `AdaptiveString("An unexpected error occurred.")`, or a raw
  `error_message` field from a backend response body)

Without a unified type, every caller that builds a dialog, snackbar, or
error banner would have to branch between `String` and `StringResource`.
`AdaptiveString` hides the distinction behind overloaded constructors and
a single `@Composable val value` accessor (plus an `async` `resolve()`
extension for SwiftUI).

**Showcase location.** See `LoginViewModel.showLoginErrorDialog` in
`feature/login`. It maps `AuthException` to a localized resource and any
other throwable to a literal fallback, feeding both into the same
`DialogUiState.message` field.

**Design principle: `Exception.message` is for logging, not UI.** The
sample-only `AuthException` in `core/data` carries a diagnostic message for
logs only. Mapping errors to user-facing text is the responsibility of the
UI layer (i.e. the ViewModel), which chooses an appropriate `AdaptiveString`
based on the exception's type — not on its `message`.

**Resource ownership.** String resources live in
`feature/*/src/commonMain/composeResources/values/strings.xml`, next to the
feature that uses them. Only the `AdaptiveString` type itself lives in
`core/ui-kit`.

## Module Structure

```
gradle-conventions       Convention Plugins (shared build configuration)
app                      Android app entry point, NavGraph, Hilt setup
shared                   Umbrella framework (ShowcaseKit) for iOS
+-- core
|   +-- foundation       KmpViewModel, Result extensions
|   +-- ui-kit           DialogPresenter, SnackbarPresenter, IndicatorState, AppTheme
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
