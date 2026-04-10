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
- **PreferenceKey / PreferenceStorage** — Type-safe wrapper for DataStore (Android) / UserDefaults (iOS)
- **DI** — Hilt (Android) + Koin (iOS)

## Module Structure

```
gradle-conventions       Convention Plugins (shared build configuration)
app                      Android app entry point, NavGraph, Hilt setup
shared                   Umbrella framework (ShowcaseKit) for iOS
+-- core
|   +-- foundation       KmpViewModel, Result extensions
|   +-- ui-kit           DialogPresenter, SnackbarPresenter, IndicatorState, AppTheme
|   +-- data             AuthRepository, PreferenceStorage (DataStore / UserDefaults)
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
| showcase.convention.kmp.feature | KMP feature module (Compose + SKIE) |
| showcase.convention.kmp.library | KMP library module |
| showcase.primitive.hilt | Hilt DI + KSP |
| showcase.primitive.spotless | Code formatting |
| showcase.primitive.detekt | Static analysis |

## Tech Stack

| Category | Library |
|----------|---------|
| Language | Kotlin 2.1 / Swift 5.9 |
| UI | Jetpack Compose (Android) / SwiftUI (iOS) |
| DI | Hilt (Android) / Koin (iOS) |
| Navigation | Navigation Compose (Android) / NavigationStack (iOS) |
| Async | Kotlin Coroutines + Flow / SKIE AsyncSequence |
| Storage | Preferences DataStore / UserDefaults |
| Build | AGP 8.14, KSP, Convention Plugins, XcodeGen |
| Code Quality | Spotless, detekt, SwiftFormat, SwiftLint |

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

# Full preflight pipeline
./script/preflight.sh
```

### Requirements

- JDK 21
- Android: minSdk 31 (Android 12)
- iOS: 17.0+
- Xcode 16+
- [Mint](https://github.com/yonaskolb/Mint) (for SwiftFormat / SwiftLint)
- [XcodeGen](https://github.com/yonaskolb/XcodeGen) (for Xcode project generation)

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
