# KMP Architecture Showcase

KMP version of android-architecture-showcase. Implements the same screens (Login / Home / Info) with Compose and SwiftUI.

## Build & Test

- Build (Android): `./gradlew assembleDebug`
- Test: `./gradlew testDebugUnitTest`
- Format: `./gradlew spotlessApply`
- Lint: `./gradlew detekt`
- iOS framework: `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64`

## Public Repository

This repository is public on GitHub.
`CLAUDE.md` is tracked in Git, so it must not contain personal dev environment details, workflows, or local paths.
Put those in `.claude/project-context.md` (gitignored).

## Commit Messages

Write commit messages in English, on a single subject line, in the present tense:
`<type>: <description>` or `<type>(<scope>): <description>`.

Types: `fix` / `feat` / `docs` / `style` / `refactor` / `test` / `build` / `perf` / `ci` / `chore`.
Do not add Git trailers such as `Co-Authored-By`.

On an `issue/<num>-<slug>` branch, prefix the subject with `#<num> ` — for example
`#4 refactor(di): migrate DI from kotlin-inject-anvil to Metro 1.4.2`.

## Adding a New Module

1. Apply a `showcase.*` convention plugin
2. namespace: `io.github.yarn44.kmp.showcase.{module.path}`
3. Add to `settings.gradle.kts`
4. For iOS: add to `shared/build.gradle.kts` export/api
