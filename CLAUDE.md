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

## Adding a New Module

1. Apply a `showcase.*` convention plugin
2. namespace: `io.github.yarn44.kmp.showcase.{module.path}`
3. Add to `settings.gradle.kts`
4. For iOS: add to `shared/build.gradle.kts` export/api
