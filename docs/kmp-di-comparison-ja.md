# KMP DI Framework Comparison

[English](kmp-di-comparison.md)

KMP プロジェクトにおける DI フレームワークの比較調査メモ。
本プロジェクトで実施した 2 本の PoC と、Hilt/Dagger の KMP 対応状況を踏まえた記録。

## 比較対象

| Framework | Version | Type | Maintainer |
|-----------|---------|------|------------|
| Hilt (Dagger) | 2.57.2 | Annotation Processor / KSP | Google |
| Koin | 3.5.6 | Service Locator (runtime) | Arnaud Giuliani |
| Metro | 0.10.4 | Kotlin Compiler Plugin | Zac Sweers |
| kotlin-inject + anvil | 0.9.0 + 0.1.7 | KSP | Eva Tatarka / Amazon |

## PoC 結果サマリー

本プロジェクトで Hilt+Koin → 統一 DI への移行 PoC を 2 本実施した。

- **PR #1**: [Metro PoC](https://github.com/44yarn/kmp-architecture-showcase/pull/1) (`feature/metro-di-poc`)
- **PR #2**: [kotlin-inject PoC](https://github.com/44yarn/kmp-architecture-showcase/pull/2) (`feature/kotlin-inject-poc`)

### 機能比較

| 観点 | Hilt+Koin (現状) | Metro 0.10.4 | kotlin-inject-anvil |
|------|-----------------|-------------|-------------------|
| KMP 統一 | **二重実装** | 統一 | 統一 |
| Android cross-module `@ContributesTo` | Hilt で動作 | **不可** (IR stub) | **動作** |
| iOS cross-module `@MergeComponent` | Koin (runtime) | 不可 (IR stub) | 不可 (klib scan) |
| `@AssistedFactory` | Dagger (JVM only) | 動作 | 動作 |
| `@Inject` の default 引数 | Hilt 不可 | **動作** | 不可 (KSP 制約) |
| コンパイル時グラフ検証 | Hilt: ✅ / Koin: ❌ | ✅ | ✅ |
| Kotlin バージョン追従 | Hilt: 安定 | **不安定** (compiler plugin ABI) | 安定 (KSP API) |
| ビルド速度 | 普通 | **最速** | やや遅い |
| 本番採用実績 | 圧倒的 | 少ない (Slack 内部?) | Bitkey (大規模 KMP), Tivi |

### iOS cross-module が両方とも動作しない理由

| Framework | 原因 | エラー |
|-----------|------|--------|
| Metro 0.10.4 | Compiler plugin が外部モジュールの IR 宣言を読めない | `IR_EXTERNAL_DECLARATION_STUB` |
| kotlin-inject-anvil 0.1.7 | KSP が klib 内の anvil メタデータをスキャンできない | `@MergeComponent` が空の interface を生成 |

**共通の fallback**: iOS グラフに全 `@Provides` をインライン記述。

### 削除できたボイラープレート (両 PoC 共通)

- `AndroidLoginViewModel.kt` (52行) — `@HiltViewModel` wrapper
- `AndroidHomeViewModel.kt` (52行) — `@HiltViewModel` wrapper
- `DispatcherProvider.kt` + `DefaultDispatcherProvider.kt` + `TestDispatcherProvider.kt`
- `KmpViewModel.kt`
- `ActivityLauncher.kt` + `ActivityLauncherImpl.kt` + `InfoActivity.kt` (Single-Activity 化)
- Hilt Module (3ファイル) + Koin Module (3ファイル) + `KoinBootstrap.kt` + `CoreKoinBridgeIos.kt`
- `hilt.gradle.kts` convention plugin

## Hilt (Dagger) の KMP 対応状況

**2025年4月時点: 公式ロードマップに載った。ただし時期未定。**

### 経緯

- **2023-06**: [Issue #3916 "KMP version of Dagger?"](https://github.com/google/dagger/issues/3916) 起票
- **2024-05**: Dagger チーム Eric Chang「KMP サポートは直近のロードマップにない」
- **2025-04**: Eric Chang が方針転換 ―「**KMP サポートは公式にロードマップに載った**」。XPoet への移行で Kotlin コード生成を推進中と説明

### 技術的なブロッカー

1. **`javax.inject` 依存** — `@Inject`, `@Qualifier` が JVM 専用。KMP の commonMain では使えない
2. **KSP2 への完全移行** — Dagger は KSP2 移行が途上
3. **XPoet 移行** — Java コード生成 → Kotlin コード生成。ほぼ完了だが後続課題あり
4. **Hilt Gradle Plugin + AGP 9** — `com.android.kotlin.multiplatform.library` との互換性未解決 ([Issue #4834](https://github.com/google/dagger/issues/4834))
5. **`@HiltViewModel` KMP 対応** — AndroidX ViewModel は KMP 化済みだが Hilt 側は未対応 ([Issue #4291](https://github.com/google/dagger/issues/4291))

### 見通し

- 全ブロッカー (KSP2 + XPoet + AGP 9) が揃う必要があり、**早くても 2026年後半〜2027年** と推測
- Google I/O 2025 でも KMP の DI には言及なし
- その頃には kotlin-inject-anvil / Metro がさらに成熟している見込み

## 判断基準

### kotlin-inject-anvil を選ぶべきケース

- KMP で DI を統一したい (**今すぐ**)
- Android の cross-module `@ContributesTo` が必要
- Kotlin upgrade 時の breakage リスクを最小化したい
- 実績ある本番事例 (Bitkey 等の大規模 KMP) に倣いたい

### Metro を選ぶべきケース

- ビルド速度が最優先
- default 引数、private injection など DX を重視
- Kotlin 2.4 stable を待てる (cross-module 改善見込み)

### Hilt+Koin のまま維持すべきケース

- Android 単体プロジェクト (KMP 化の予定なし)
- プロジェクト規模が小さく、二重実装のコストが無視できる
- Hilt の KMP 対応を待てる (2026年後半〜?)

## 本プロジェクトの結論

**kotlin-inject-anvil が最も production-ready。** 理由:

1. Metro の最大の制限 (Android cross-module) を解決
2. iOS の制限は Metro と同じ (共通の fallback で対処可能)
3. KSP ベースで Kotlin version 追従が安定
4. Bitkey 等の大規模 KMP プロジェクトでの本番実績

## 参考リンク

### PoC レポート (各ブランチ)
- [Metro PoC Report (EN)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/metro-di-poc/docs/metro-migration.md)
- [Metro PoC Report (JA)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/metro-di-poc/docs/metro-migration-ja.md)
- [kotlin-inject PoC Report (EN)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/kotlin-inject-poc/docs/kotlin-inject-migration.md)
- [kotlin-inject PoC Report (JA)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/kotlin-inject-poc/docs/kotlin-inject-migration-ja.md)

### 公式リソース
- [google/dagger — Issue #3916 (KMP)](https://github.com/google/dagger/issues/3916)
- [google/dagger — Issue #4834 (Hilt + Android-KMP plugin)](https://github.com/google/dagger/issues/4834)
- [evant/kotlin-inject](https://github.com/evant/kotlin-inject)
- [amzn/kotlin-inject-anvil](https://github.com/amzn/kotlin-inject-anvil)
- [ZacSweers/metro](https://github.com/ZacSweers/metro)

### 採用事例・記事
- [Introducing kotlin-inject-anvil (Ralf Wondratschek)](https://ralf-wondratschek.com/blog/introducing-kotlin-inject-anvil)
- [Introducing Metro (Zac Sweers)](https://www.zacsweers.dev/introducing-metro/)
- [tv-maniac kotlin-inject-anvil integration (ProAndroidDev)](https://proandroiddev.com/integrate-kotlin-inject-anvil-to-tv-maniac-e1330c9cb566)
- [Dagger/Hilt → kotlin-inject migration (ProAndroidDev)](https://proandroiddev.com/from-dagger-hilt-into-the-multiplatform-world-with-kotlin-inject-647d8e3bddd5)

---

*Last updated: 2026-04-14*
