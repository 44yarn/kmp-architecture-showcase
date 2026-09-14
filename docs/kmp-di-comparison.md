# KMP DI Framework Comparison

[Japanese / 日本語](kmp-di-comparison-ja.md)

Comparison notes on DI frameworks for KMP projects, based on PoCs conducted in this
repository plus an investigation into Hilt/Dagger's KMP roadmap.

> **Correction (2026-08).** The 2026-04 revision of this document concluded that
> kotlin-inject-anvil was the most production-ready option, partly because the Metro PoC
> (#1) failed to get cross-module `@ContributesTo` aggregation working on either Android
> or iOS. **That failure was a version prerequisite, not a Metro limitation.** Metro's
> multi-module aggregation requires **Kotlin 2.3.20+ on Android and Apple targets**; the
> PoC ran on Kotlin 2.3.10. Re-run on Kotlin 2.4.10 with Metro 1.4.2, aggregation works on
> both platforms. See [Corrected findings](#corrected-findings-2026-08).

## Frameworks Compared

| Framework | Version | Type | Maintainer |
|-----------|---------|------|------------|
| Hilt (Dagger) | 2.57.2 | Annotation Processor / KSP | Google |
| Koin | 3.5.6 | Service Locator (runtime) | Arnaud Giuliani |
| Metro | 1.4.2 | Kotlin Compiler Plugin | Zac Sweers |
| kotlin-inject + anvil | 0.9.0 + 0.1.7 | KSP | Eva Tatarka / Ralf Wondratschek |

## Upstream status (2026-08)

The single biggest change since the 2026-04 revision is that the two projects swapped
positions on maintenance health.

### kotlin-inject-anvil: maintenance mode

The [README](https://github.com/amzn/kotlin-inject-anvil) was updated on 2026-07-15:

> kotlin-inject-anvil is in maintenance mode. The last meaningful release was over a year
> ago. ... I'm happy to make critical bug fixes in case the KSP integration breaks, but
> nothing further.
>
> **We moved our projects from kotlin-inject-(anvil) to Metro**, which is a better
> solution long term.

Ownership also moved away from Amazon; the project is now maintained independently at
`vRallev/kotlin-inject-anvil`. Package names and Maven coordinates still use
`software.amazon.*` for compatibility.

This invalidates the "large-scale KMP production track record" argument that the 2026-04
revision used to pick kotlin-inject-anvil — the team behind that track record moved to
Metro.

`evant/kotlin-inject` itself has had no commit since **2026-01-07** (v0.9.0).

### Metro: 1.0 stable and actively developed

- **1.0.0 stable on 2026-04-27** (two weeks after PoC #1 was run), 1.4.2 on 2026-08-13.
- Stability is scoped to the **ABI of the runtime artifacts** (runtime, metrox, Gradle
  plugin). The compiler plugin itself is not covered — Kotlin compatibility is a moving
  window of roughly five compiler versions, documented in
  [`docs/compatibility.md`](https://github.com/ZacSweers/metro/blob/main/docs/compatibility.md).
- The kotlin-inject-anvil author now contributes to Metro.

### Hilt (Dagger): still not shipped

Latest word from the Dagger team on
[google/dagger#3916](https://github.com/google/dagger/issues/3916) (**2026-08-11**):

> yes, this is still on the roadmap. The progress slowed/stopped due to multiple issues --
> first, the switch from KSP1 to KSP2, and then the shift to focus on KSP performance
> issues. However, the performance work is wrapping up and we are now in a position to get
> back to this soon.

No date. The 2026-04 estimate of "late 2026 – 2027 at the earliest" should be read as
later still.

### Koin: now has a compiler plugin

[`InsertKoinIO/koin-compiler-plugin`](https://github.com/InsertKoinIO/koin-compiler-plugin)
reached **1.1.0**. Koin is moving from a KSP/annotations model to a native Kotlin compiler
plugin that resolves `single<T>()` at compile time. This makes the "Koin = runtime service
locator with no compile-time verification" row in older comparison tables out of date. It
is young (well under 100 stars) and was not evaluated here.

## Corrected findings (2026-08)

Two experiment branches were built from the same base, differing only in the DI framework,
and both were verified green on `assembleDebug` +
`:shared:linkDebugFrameworkIosSimulatorArm64`.

### Cross-module aggregation works on iOS

Metro's [`docs/multiplatform.md`](https://github.com/ZacSweers/metro/blob/main/docs/multiplatform.md)
states the Kotlin floor for multi-module aggregation per target:

| Target | Minimum Kotlin |
|---|---|
| JVM | 2.3.0 |
| **Android** | **2.3.20** |
| **Apple** | **2.3.20** |
| JS | 2.3.21 |
| Wasm / Linux / Windows / Android Native | 2.3.20 |

PoC #1 ran on Kotlin **2.3.10**, below the floor for both platforms it tested. The two
corresponding upstream issues are closed:

- [ZacSweers/metro#460](https://github.com/ZacSweers/metro/issues/460) — aggregation
  limited to jvm/android — closed 2026-01-20
- [ZacSweers/metro#1556](https://github.com/ZacSweers/metro/issues/1556) — native klib
  serialization dropping qualifier annotations — closed 2026-01-26

On Kotlin 2.4.10 + Metro 1.4.2, the iOS graph declares **no** `@Provides` at all; all six
providers are aggregated from `@ContributesTo` interfaces in other modules, exactly like
the Android graph.

### Measured effect on this codebase

| Metric | kotlin-inject-anvil | Metro 1.4.2 | Delta |
|---|---|---|---|
| DI declaration code | 276 lines | 201 lines | **−75 (−27%)** |
| iOS graph (single file) | 105 lines | 41 lines | −64 |
| Per-target `expect/actual` factories | 3 files | 0 | `createGraph<T>()` replaces them |
| Gradle DI wiring | 69 lines | 13 lines | −56 |
| Per-module Gradle DI wiring | 9 lines | 1 line | plugin id only |
| Overall diff | — | — | **+72 / −272** |

Migration cost was low: in the per-module contribution interfaces the annotation surface is
identical, so the change is a pure import swap. Only three things needed real rewriting —
the Android graph (`@MergeComponent` + constructor input → `@DependencyGraph` +
`@DependencyGraph.Factory`), the iOS graph, and `@Inject` → `@AssistedInject` on the
assisted-injected ViewModel.

### Build speed: no difference at this size

Clean builds, build cache disabled, three runs each:

| | run 1 | run 2 | run 3 | mean |
|---|---|---|---|---|
| kotlin-inject (KSP) | 18s | 16s | 17s | **17.0s** |
| Metro | 16s | 17s | 18s | **17.0s** |

Metro's advertised 50–80% build-time improvement **does not materialize on a ~3,200-line
project**. Build speed is not a valid reason to migrate at this scale.

### The iOS fallback had produced dead code

Because the iOS graph used a plain `@Component` (no merge), the `@ContributesTo` interface
`IosDataStoreComponent` was unreachable — 41 lines with zero references. Its
`providePreferencesPath()` NSDocumentDirectory logic existed twice: one live copy inlined
in the graph, one dead copy in the module. Aggregation collapses this to a single source.
This is the concrete maintenance cost of an "inline every provider" fallback.

## Toolchain constraints (independent of the DI choice)

Found while building the experiment branches; these apply regardless of which DI framework
is used.

- **KSP must stay at 2.3.6.** KSP 2.3.7 raised its Kotlin target language version to 2.3,
  which the `gradle-conventions` precompiled-script-plugin build (Gradle 8.14.3, embedded
  Kotlin 2.0) cannot read. Metro needs no KSP at all, so this pin disappears with it.
- **`iosX64` must be dropped.** Compose Multiplatform 1.11.0 no longer ships Apple x86_64
  artifacts ([KT-81596](https://youtrack.jetbrains.com/issue/KT-81596)). Removing that one
  target required editing `kspIosX64` wiring in six modules under kotlin-inject; under
  Metro there is no such wiring.
- **kotlin-inject still works on Kotlin 2.4.10.** Verified green. A stalled upstream is not
  the same as a broken build — there was no forced migration here.
- **Metro's Gradle plugin cannot be wrapped in a precompiled script plugin** (it requires
  the Kotlin plugin to already be applied), so it is applied directly in each module's
  `plugins {}` block.

## Decision criteria

### Choose Metro when

- You want unified KMP DI today, including cross-module aggregation on Apple targets
- You can run **Kotlin 2.3.20+** (2.3.0+ if JVM-only)
- You want to drop KSP from the build entirely
- You accept that compiler-plugin compatibility is a moving window and Kotlin upgrades may
  need a matching Metro bump

### Choose kotlin-inject-anvil when

- You are already on it and there is no pressure to move — it still builds fine on current
  Kotlin
- You need to stay on Kotlin < 2.3.20 and still want cross-module aggregation on Android
- You prefer KSP's more conservative compatibility story over a compiler plugin

Note that new adoption is hard to justify given maintenance mode plus the upstream author's
own recommendation.

### Stay on Hilt+Koin when

- Android-only project with no KMP plans
- The project is small enough that the dual implementation cost is negligible

## Conclusion for this project

**Migrated to Metro** in
[#4](https://github.com/44yarn/kmp-architecture-showcase/issues/4). The reasons are
upstream health and structural duplication, **not** build speed:

1. kotlin-inject-anvil is in maintenance mode and its author recommends Metro
2. Cross-module aggregation now works on iOS, which removes the inlined-provider fallback
   and the dead code it produced
3. KSP leaves the build entirely, along with the per-module, per-target wiring it required
4. Metro is 1.0 stable with an active release cadence

## References

### PoC reports (per branch)
- [Metro PoC Report (EN)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/metro-di-poc/docs/metro-migration.md)
- [Metro PoC Report (JA)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/metro-di-poc/docs/metro-migration-ja.md)
- [kotlin-inject PoC Report (EN)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/kotlin-inject-poc/docs/kotlin-inject-migration.md)
- [kotlin-inject PoC Report (JA)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/kotlin-inject-poc/docs/kotlin-inject-migration-ja.md)

### Official resources
- [ZacSweers/metro](https://github.com/ZacSweers/metro) — [compatibility](https://github.com/ZacSweers/metro/blob/main/docs/compatibility.md) / [multiplatform](https://github.com/ZacSweers/metro/blob/main/docs/multiplatform.md)
- [Metro is stable (Zac Sweers)](https://www.zacsweers.dev/metro-is-stable/)
- [amzn/kotlin-inject-anvil](https://github.com/amzn/kotlin-inject-anvil) — maintenance mode notice
- [evant/kotlin-inject](https://github.com/evant/kotlin-inject)
- [google/dagger#3916 (KMP)](https://github.com/google/dagger/issues/3916)
- [google/dagger#4834 (Hilt + Android-KMP plugin)](https://github.com/google/dagger/issues/4834)
- [InsertKoinIO/koin-compiler-plugin](https://github.com/InsertKoinIO/koin-compiler-plugin)

### Adoption reports / articles
- [Introducing kotlin-inject-anvil (Ralf Wondratschek)](https://ralf-wondratschek.com/blog/introducing-kotlin-inject-anvil)
- [Introducing Metro (Zac Sweers)](https://www.zacsweers.dev/introducing-metro/)
- [tv-maniac kotlin-inject-anvil integration (ProAndroidDev)](https://proandroiddev.com/integrate-kotlin-inject-anvil-to-tv-maniac-e1330c9cb566)
- [Dagger/Hilt → kotlin-inject migration (ProAndroidDev)](https://proandroiddev.com/from-dagger-hilt-into-the-multiplatform-world-with-kotlin-inject-647d8e3bddd5)

---

*Last updated: 2026-08-22*
