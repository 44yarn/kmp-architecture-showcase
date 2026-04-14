# KMP DI Framework Comparison

[Japanese / 日本語](kmp-di-comparison-ja.md)

Comparison notes on DI frameworks for KMP projects.
Based on two PoCs conducted in this project and an investigation into Hilt/Dagger's KMP roadmap.

## Frameworks Compared

| Framework | Version | Type | Maintainer |
|-----------|---------|------|------------|
| Hilt (Dagger) | 2.57.2 | Annotation Processor / KSP | Google |
| Koin | 3.5.6 | Service Locator (runtime) | Arnaud Giuliani |
| Metro | 0.10.4 | Kotlin Compiler Plugin | Zac Sweers |
| kotlin-inject + anvil | 0.9.0 + 0.1.7 | KSP | Eva Tatarka / Amazon |

## PoC Results Summary

Two PoCs migrating from Hilt+Koin to a unified DI framework were conducted in this project.

- **PR #1**: [Metro PoC](https://github.com/44yarn/kmp-architecture-showcase/pull/1) (`feature/metro-di-poc`)
- **PR #2**: [kotlin-inject PoC](https://github.com/44yarn/kmp-architecture-showcase/pull/2) (`feature/kotlin-inject-poc`)

### Feature Comparison

| Aspect | Hilt+Koin (current) | Metro 0.10.4 | kotlin-inject-anvil |
|--------|-------------------|-------------|-------------------|
| KMP unified | **Dual implementation** | Unified | Unified |
| Android cross-module `@ContributesTo` | Works via Hilt | **Does not work** (IR stub) | **Works** |
| iOS cross-module `@MergeComponent` | Koin (runtime) | Does not work (IR stub) | Does not work (klib scan) |
| `@AssistedFactory` | Dagger (JVM only) | Works | Works |
| Default args in `@Inject` | Hilt: no | **Works** | No (KSP limitation) |
| Compile-time graph validation | Hilt: yes / Koin: no | Yes | Yes |
| Kotlin version tracking | Hilt: stable | **Unstable** (compiler plugin ABI) | Stable (KSP API) |
| Build speed | Normal | **Fastest** | Slightly slower |
| Production adoption | Dominant | Limited (Slack internal?) | Bitkey (large-scale KMP), Tivi |

### Why iOS Cross-Module Fails in Both

| Framework | Cause | Error |
|-----------|-------|-------|
| Metro 0.10.4 | Compiler plugin cannot read IR declarations from external modules | `IR_EXTERNAL_DECLARATION_STUB` |
| kotlin-inject-anvil 0.1.7 | KSP cannot scan anvil metadata inside klibs | `@MergeComponent` generates empty interface |

**Common fallback**: Inline all `@Provides` into the iOS graph.

### Boilerplate Eliminated (Common to Both PoCs)

- `AndroidLoginViewModel.kt` (52 lines) — `@HiltViewModel` wrapper
- `AndroidHomeViewModel.kt` (52 lines) — `@HiltViewModel` wrapper
- `DispatcherProvider.kt` + `DefaultDispatcherProvider.kt` + `TestDispatcherProvider.kt`
- `KmpViewModel.kt`
- `ActivityLauncher.kt` + `ActivityLauncherImpl.kt` + `InfoActivity.kt` (Single-Activity refactor)
- Hilt Modules (3 files) + Koin Modules (3 files) + `KoinBootstrap.kt` + `CoreKoinBridgeIos.kt`
- `hilt.gradle.kts` convention plugin

## Hilt (Dagger) KMP Roadmap

**As of April 2025: Officially on the roadmap. No timeline given.**

### Timeline

- **2023-06**: [Issue #3916 "KMP version of Dagger?"](https://github.com/google/dagger/issues/3916) opened
- **2024-05**: Dagger team (Eric Chang): "KMP support is not on the near-term roadmap"
- **2025-04**: Eric Chang reverses course — "**KMP support is officially on the roadmap**". XPoet migration underway for Kotlin code generation

### Technical Blockers

1. **`javax.inject` dependency** — `@Inject`, `@Qualifier` are JVM-only. Cannot be used in commonMain
2. **KSP2 migration** — Dagger's KSP2 migration is in progress
3. **XPoet migration** — Java → Kotlin code generation. Nearly complete but follow-up issues remain
4. **Hilt Gradle Plugin + AGP 9** — Incompatible with `com.android.kotlin.multiplatform.library` ([Issue #4834](https://github.com/google/dagger/issues/4834))
5. **`@HiltViewModel` KMP** — AndroidX ViewModel is KMP-ready but Hilt is not ([Issue #4291](https://github.com/google/dagger/issues/4291))

### Outlook

- All blockers (KSP2 + XPoet + AGP 9) must be resolved — **earliest estimate: late 2026 to 2027**
- Google I/O 2025 did not mention DI for KMP
- By then kotlin-inject-anvil and Metro will be further matured

## Decision Criteria

### Choose kotlin-inject-anvil when:

- You want unified KMP DI **now**
- Android cross-module `@ContributesTo` is needed
- You want to minimize Kotlin upgrade breakage risk
- You want to follow proven production usage (Bitkey and other large-scale KMP projects)

### Choose Metro when:

- Build speed is the top priority
- DX matters (default args, private injection)
- You can wait for Kotlin 2.4 stable (cross-module fix expected)

### Stay with Hilt+Koin when:

- Android-only project (no KMP plans)
- Small project where dual implementation cost is negligible
- You can wait for Hilt KMP (late 2026+?)

## Conclusion for This Project

**kotlin-inject-anvil is the most production-ready option.** Reasons:

1. Solves Metro's biggest limitation (Android cross-module)
2. Shares the same iOS limitation as Metro (common fallback available)
3. Stable Kotlin version tracking via KSP
4. Proven in large-scale KMP production (Bitkey, Tivi, etc.)

## References

### PoC Reports (per branch)
- [Metro PoC Report (EN)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/metro-di-poc/docs/metro-migration.md)
- [Metro PoC Report (JA)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/metro-di-poc/docs/metro-migration-ja.md)
- [kotlin-inject PoC Report (EN)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/kotlin-inject-poc/docs/kotlin-inject-migration.md)
- [kotlin-inject PoC Report (JA)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/kotlin-inject-poc/docs/kotlin-inject-migration-ja.md)

### Official Resources
- [google/dagger — Issue #3916 (KMP)](https://github.com/google/dagger/issues/3916)
- [google/dagger — Issue #4834 (Hilt + Android-KMP plugin)](https://github.com/google/dagger/issues/4834)
- [evant/kotlin-inject](https://github.com/evant/kotlin-inject)
- [amzn/kotlin-inject-anvil](https://github.com/amzn/kotlin-inject-anvil)
- [ZacSweers/metro](https://github.com/ZacSweers/metro)

### Adoption Examples & Articles
- [Introducing kotlin-inject-anvil (Ralf Wondratschek)](https://ralf-wondratschek.com/blog/introducing-kotlin-inject-anvil)
- [Introducing Metro (Zac Sweers)](https://www.zacsweers.dev/introducing-metro/)
- [tv-maniac kotlin-inject-anvil integration (ProAndroidDev)](https://proandroiddev.com/integrate-kotlin-inject-anvil-to-tv-maniac-e1330c9cb566)
- [Dagger/Hilt to kotlin-inject migration (ProAndroidDev)](https://proandroiddev.com/from-dagger-hilt-into-the-multiplatform-world-with-kotlin-inject-647d8e3bddd5)

---

*Last updated: 2026-04-14*
