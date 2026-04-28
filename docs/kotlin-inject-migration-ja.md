# kotlin-inject DI 移行 — PoC レポート

[English](kotlin-inject-migration.md)

## 概要

このブランチは、プロジェクトの DI を **Hilt (Android) + Koin (iOS)** 二重構成から **kotlin-inject 0.9.0 + kotlin-inject-anvil 0.1.7** (KMP 統一、KSP によるコンパイル時グラフ検証) に移行する。

kotlin-inject は [Eva Tatarka](https://github.com/evant/kotlin-inject) によるコンパイル時 DI フレームワーク。kotlin-inject-anvil は [Amazon](https://github.com/amzn/kotlin-inject-anvil) によるクロスモジュール contribution マージ拡張。

### なぜマージしないか？

この PR は Metro PoC (`feature/metro-di-poc`) と並ぶ参照実装として残す。2つの KMP DI アプローチの比較材料。

**マージ条件:**
- kotlin-inject-anvil と Metro の比較に基づくチーム判断
- iOS クロスモジュール `@MergeComponent` のサポート改善（現状はインライン fallback が必要）
- DI 戦略の合意

---

## 移行ステージ

| Stage | 内容 | コミット |
|---|---|---|
| 0 | プラグインインフラ | `chore(gradle): add kotlin-inject + anvil plugin infrastructure` |
| 1 | `DispatcherProvider` を `@IoDispatcher` に置換 | `refactor(core): replace DispatcherProvider with kotlin-inject @IoDispatcher` |
| 1 | core/data の DI 移行 | `refactor(core-data): migrate DI to kotlin-inject-anvil @ContributesTo` |
| 2 | Login 移行 + Single-Activity 化 | `refactor(feature-login): ...` + `refactor: collapse to single-activity` |
| 2 | `@MergeComponent` 追加 | `feat(app): add @MergeComponent and remove Hilt entry points` |
| 3 | Home の Assisted Factory 移行 | `refactor(feature-home): migrate to kotlin-inject @AssistedFactory` |
| 4 | iOS の Koin → kotlin-inject 置換 | `refactor(ios): replace Koin with kotlin-inject IosAppComponent` |
| 5 | Hilt + Koin 完全削除 | `chore: remove Hilt, Koin, and KmpViewModel dependencies` |
| fix | `kspCommonMainMetadata` 削除 | `fix(build): remove kspCommonMainMetadata to avoid anvil redeclaration` |

---

## 主要な発見

### 1. Android クロスモジュール `@ContributesTo` + `@MergeComponent` は完全動作

kotlin-inject-anvil の `@ContributesTo` は、`core/data`、`feature/login`、`feature/home` で宣言した contribution を `app` モジュールの `ShowcaseAppComponent` (`@MergeComponent`) に自動マージする。これが **Metro 0.10.4 に対する最大の優位点** — Metro では `IR_EXTERNAL_DECLARATION_STUB` で cross-module `@ContributesTo` が失敗していた。

生成された `KotlinInjectShowcaseAppComponentMerged` は全 contribution を正しく継承:
```kotlin
public interface KotlinInjectShowcaseAppComponentMerged :
    IoGithubYarn44KmpShowcaseFeatureLoginDiLoginComponent,
    IoGithubYarn44KmpShowcaseCoreDataDiDataComponent,
    IoGithubYarn44KmpShowcaseFeatureHomeDiHomeComponent,
    IoGithubYarn44KmpShowcaseCoreDataDiAndroidDataStoreComponent
```

### 2. iOS クロスモジュール `@MergeComponent` は動作しない

iOS ターゲットでは `@MergeComponent` が**空のマージ interface** を生成する。KSP プロセッサが他モジュールの klib 出力から `@ContributesTo` contribution を発見できない。

**原因:** KSP の classpath スキャンは JAR (Android/JVM) で動作するが、klib (Kotlin/Native) では動作しない。`core/data` の KSP が生成した anvil メタデータは klib にコンパイルされるが、`shared` の KSP プロセッサがそれをスキャンできない。

**回避策:** iOS は `@Component` に全 `@Provides` をインラインで記述 — Metro PoC と同じ fallback パターン。

**参考:** [kotlin-inject-anvil Issue #118](https://github.com/amzn/kotlin-inject-anvil/issues/118)

### 3. `kspCommonMainMetadata` と `kspIosXxx` の同時使用は不可

両方の設定を使うと kotlin-inject-anvil で Redeclaration エラーが発生する。正しいアプローチは **ターゲット別 KSP のみ** (`kspAndroid`, `kspIosArm64` 等) を使用すること。

### 4. `@AssistedFactory` は commonMain で動作する

kotlin-inject 0.8.0+ の `@Assisted` + `@AssistedFactory` は全 KMP ターゲットで動作。`HomeViewModel.Factory` は `commonMain` で宣言し、Android と iOS の両方で動作する。

### 5. `@Inject` コンストラクタの default 引数は使えない (KSP 制約)

Metro と異なり、kotlin-inject は KSP 経由でデフォルト引数値を参照できない。`IndicatorState` や `DialogPresenter` は `@Provides` メソッドで明示的に提供する必要がある。

### 6. iOS では `create()` に `expect/actual` パターンが必要

KSP は `IosAppComponent::class.create()` を各 iOS ターゲット source set (`iosSimulatorArm64Main` 等) に生成するが、この extension は `iosMain` intermediate source set からは参照できない。解決策:
- `iosMain`: `internal expect fun createIosAppComponent(): IosAppComponent`
- 各ターゲット: `internal actual fun createIosAppComponent() = IosAppComponent::class.create()`

### 7. 移行の副次的効果 (Metro PoC と同じ)

- **Single-Activity アーキテクチャ**: `InfoActivity` と `ActivityLauncher` を削除、全画面を 1 つの `NavHost` に統合
- **`KmpViewModel` 削除**: ViewModel は `androidx.lifecycle.ViewModel` を直接継承
- **Android `@HiltViewModel` ラッパー削除**: `AndroidLoginViewModel` (52行) と `AndroidHomeViewModel` (52行) を削除

---

## 移行後のアーキテクチャ

### Android

```
ShowcaseApplication
  └── appComponent: ShowcaseAppComponent (lazy, @MergeComponent)
        ├── マージ: DataComponent (@ContributesTo from core/data/commonMain)
        ├── マージ: AndroidDataStoreComponent (@ContributesTo from core/data/androidMain)
        ├── マージ: LoginComponent (@ContributesTo from feature/login/commonMain)
        └── マージ: HomeComponent (@ContributesTo from feature/home/commonMain)

MainActivity
  └── setContentWithTheme {
        ShowcaseNavGraph(appComponent = appComponent)
          ├── LoginScreen(viewModel = viewModel { appComponent.loginViewModel })
          ├── HomeScreen(viewModel = viewModel { appComponent.homeViewModelFactory.create(...) })
          └── InfoScreen(onBack = ...)
      }
```

### iOS

```
IosAppComponent (@Component, singleton — klib 制限によりプロバイダーをインライン記述)
  ├── @Provides fun provideIoDispatcher()
  ├── @Provides fun provideDataStore()
  ├── @Provides fun providePreferenceStorage()
  ├── @Provides fun provideIndicatorState()
  ├── @Provides fun provideDialogPresenter()
  ├── @Provides fun provideSnackbarPresenter()
  ├── val loginViewModel: LoginViewModel
  └── val homeViewModelFactory: HomeViewModel.Factory

Top-level ヘルパー (Swift 向け):
  ├── bootstrapIosAppComponent() → ShowcaseApp.init
  ├── getLoginViewModel()        → LoginView
  └── getHomeViewModel(...)      → HomeView
```

---

## Metro PoC との比較

| 観点 | kotlin-inject-anvil | Metro 0.10.4 |
|------|-------------------|-------------|
| Android クロスモジュール `@ContributesTo` | **動作** (自動マージ) | 動作しない (インライン fallback) |
| iOS クロスモジュール `@MergeComponent` | 動作しない (klib 制限) | 動作しない (IR stub 制限) |
| `@AssistedFactory` | 動作 (KSP) | 動作 (compiler plugin) |
| `@Inject` の default 引数 | 非対応 (KSP 制約) | **対応** (IR アクセス) |
| Kotlin バージョン互換性 | 安定 (KSP API) | 不安定 (compiler plugin ABI) |
| ビルド速度 | やや遅い (KSP overhead) | **高速** (compiler plugin) |
| 本番採用実績 | Bitkey (170 modules), Tivi | DroidKaigi 2025 |
| 成熟度 | 0.9.0 + anvil 0.1.7 | 0.10.4 (pre-1.0) |

**要点:** kotlin-inject-anvil は Metro の最大の制限 (Android クロスモジュール) を解決しつつ、iOS では同じ制限を共有する。トレードオフは default 引数非対応とやや遅いビルド速度。

---

## ファイル変更サマリー

### 削除

| ファイル | 理由 |
|---|---|
| `AndroidLoginViewModel.kt` | `LoginViewModel` の `@Inject` で代替 |
| `AndroidHomeViewModel.kt` | `@AssistedFactory` で代替 |
| `DispatcherProvider.kt` | `@Qualifier @IoDispatcher` で代替 |
| `TestDispatcherProvider.kt` | テストで `StandardTestDispatcher` を直接渡す |
| `KmpViewModel.kt` | ViewModel は `ViewModel()` を直接継承 |
| `ActivityLauncher.kt` | Single-Activity リファクタ |
| `ActivityLauncherImpl.kt` | Single-Activity リファクタ |
| `ActivityLauncherModule.kt` | Single-Activity + Hilt 削除 |
| `InfoActivity.kt` | Single-Activity リファクタ |
| `PreferenceModule.kt` (Hilt) | `DataComponent` + `AndroidDataStoreComponent` で代替 |
| `RepositoryModule.kt` (Hilt) | `DataComponent` で代替 |
| `hilt.gradle.kts` | `kotlin-inject.gradle.kts` で代替 |
| `KoinBootstrap.kt` | `IosAppComponent.kt` で代替 |
| `CoreKoinBridgeIos.kt` | `IosAppComponent.kt` で代替 |
| `CoreDataKoinModule.kt` | `DataComponent` + `IosDataStoreComponent` で代替 |
| `FeatureLoginKoinModule.kt` | `LoginComponent` で代替 |
| `FeatureHomeKoinModule.kt` | `HomeComponent` で代替 |

### 新規作成

| ファイル | 目的 |
|---|---|
| `ShowcaseAppComponent.kt` | Android `@MergeComponent` (クロスモジュール自動マージ) |
| `IosAppComponent.kt` | iOS `@Component` + Swift ヘルパー |
| `IosAppComponentFactory.kt` (x3) | iOS ターゲットごとの `expect/actual` |
| `IoDispatcher.kt` | kotlin-inject `@Qualifier` (commonMain) |
| `DataComponent.kt` | `@ContributesTo` — IoDispatcher + PreferenceStorage |
| `AndroidDataStoreComponent.kt` | `@ContributesTo` — Android DataStore |
| `IosDataStoreComponent.kt` | `@ContributesTo` — iOS DataStore |
| `LoginComponent.kt` | `@ContributesTo` — IndicatorState + DialogPresenter + LoginViewModel |
| `HomeComponent.kt` | `@ContributesTo` — SnackbarPresenter + HomeViewModel.Factory |
| `InfoRoute.kt` | Single-Activity 用 Navigation route |
| `kotlin-inject.gradle.kts` | KSP 用 Convention plugin |

---

## KSP 設定の注意事項

### ターゲット別 KSP (必須パターン)

kotlin-inject アノテーションを使う各 KMP モジュールは、**ターゲットごとに個別に** KSP を設定する:

```kotlin
dependencies {
    add("kspAndroid", libs.kotlinInjectCompiler)
    add("kspAndroid", libs.kotlinInjectAnvilCompiler)
    listOf("kspIosX64", "kspIosArm64", "kspIosSimulatorArm64").forEach { config ->
        add(config, libs.kotlinInjectCompiler)
        add(config, libs.kotlinInjectAnvilCompiler)
    }
}
```

`kspCommonMainMetadata` をターゲット別 KSP と同時に使用**しないこと** — Redeclaration エラーの原因になる。

---

## 参考リンク

- [kotlin-inject GitHub](https://github.com/evant/kotlin-inject)
- [kotlin-inject-anvil GitHub](https://github.com/amzn/kotlin-inject-anvil)
- [kotlin-inject multiplatform docs](https://github.com/evant/kotlin-inject/blob/main/docs/multiplatform.md)
- [Introducing kotlin-inject-anvil (Ralf Wondratschek)](https://ralf-wondratschek.com/blog/introducing-kotlin-inject-anvil)
- [kotlin-inject-anvil Issue #118 (クロスモジュール KMP)](https://github.com/amzn/kotlin-inject-anvil/issues/118)
- [Metro PoC レポート](../metro-migration-ja.md) (feature/metro-di-poc ブランチ)
