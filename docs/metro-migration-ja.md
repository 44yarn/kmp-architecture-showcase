# Metro DI 移行 — PoC レポート

[English](metro-migration.md)

## 概要

本ブランチでは、プロジェクトの DI を **Hilt (Android) + Koin (iOS)** の二重構成から **Metro 0.10.4** (KMP 統一、コンパイル時グラフ検証) に移行しています。

Metro は [Zac Sweers](https://github.com/ZacSweers/metro) による Kotlin compiler plugin ベースの DI フレームワークで、Dagger, Anvil, kotlin-inject にインスパイアされています。

### なぜマージしないのか

Metro は **pre-1.0** (0.10.4) であり、既知の制約があります。本 PR は「KMP 統一 DI がどのような形になるか」のリファレンス実装として open のまま維持し、production 推奨の構成としては扱いません。

**マージ条件:**
- Kotlin 2.4 stable + Compose Multiplatform の Kotlin 2.4 対応
- Metro 0.13.x+ (or 1.0) で cross-module `@ContributesTo` が修正されること
- この 3 条件が揃えば、本プロジェクトで Metro を production-ready として採用可能

---

## 移行ステージ

| Stage | 内容 | Commit |
|---|---|---|
| 0 | Metro プラグイン基盤 | `chore(gradle): add Metro DI plugin infrastructure` |
| 1 | `DispatcherProvider` を `@IoDispatcher` に置換 | `refactor(core): replace DispatcherProvider with Metro @IoDispatcher` |
| 2 | Login の Metro 化 + Single-Activity 化 | `build: downgrade Metro to 0.10.4...` + `refactor: migrate login to Metro DI...` |
| 3 | Home の assisted factory 移行 | `refactor: migrate home to Metro assisted DI` |
| 4 | iOS の Koin を Metro に置換 | `refactor(ios): migrate from Koin to Metro via IosAppGraph` |
| 4-fix | クロスモジュール multibinding crash 修正 | `fix: use direct graph accessors instead of cross-module multibinding` |
| 5 | Hilt + Koin 完全削除 | `chore: remove Hilt, Koin, and metrox-viewmodel dependencies` |

---

## 主な発見

### 1. Kotlin バージョン互換性

Metro 0.13.x は **Kotlin 2.4.0-Beta1 以上** を要求します (2.3.x とは IR API の不一致)。Compose Multiplatform 1.10.3 が Kotlin 2.4 をまだサポートしていないため、**Kotlin 2.3.10** と互換性のある **Metro 0.10.4** にダウングレードしました。

### 2. クロスモジュール `@ContributesTo` / `@ContributesIntoMap` 制約

Metro 0.10.4 の compiler plugin は、`@DependencyGraph` とは別の Gradle モジュールに宣言された `@ContributesTo` / `@ContributesIntoMap` の contribution を解決できません。plugin がこれらの宣言に `IR_EXTERNAL_DECLARATION_STUB` 経由でアクセスし、`IllegalStateException` で失敗します。

**回避策:** すべての `@Provides` 関数を graph interface (`ShowcaseAppGraph` (Android) / `IosAppGraph` (iOS)) 内にインライン化。ViewModel は multibinding map ではなく直接 accessor (`val loginViewModel: LoginViewModel`) で公開。

### 3. `@Inject constructor` の default 引数サポート

Dagger/Hilt とは異なり、Metro は `@Inject` constructor に default 引数を許可します。`LoginViewModel(..., indicatorState: IndicatorState = IndicatorState())` のように書けるため、default 化されたパラメータに `@Provides` や `@AssistedInject` を使う必要がありません。

### 4. `@AssistedFactory` が commonMain で動作

Metro の `@AssistedInject` + `@AssistedFactory` は compiler plugin で全 KMP ターゲットに対して処理されます。`HomeViewModel.Factory` は `commonMain` に宣言し、Android/iOS 両方で動作します。

### 5. metrox-viewmodel-compose は最終的に不要

クロスモジュール制約 (#2) のため `MetroViewModelFactory` の multibinding 方式が動作せず、direct graph accessor + 標準 `viewModel { }` composable に切り替えました。結果として `metrox-viewmodel-compose` は不要になりました。

### 6. iOS Swift 連携

- Kotlin の `init` で始まる関数名は Swift で `doInit...` に変換されます。`bootstrap` など別の動詞を使います。
- Kotlin/Native の `Dispatchers.IO` は `internal` です。`kotlinx.coroutines.IO` extension import を使います。
- iOS graph は `createGraph<IosAppGraph>()` で生成 (runtime パラメータ不要のため factory 不要)。

### 7. 移行の副次効果

- **Single-Activity 化**: `InfoActivity` と `ActivityLauncher` を削除し、全画面を 1 つの `NavHost` に統合。
- **`KmpViewModel` が `androidx.lifecycle.ViewModel` を継承**: Android ViewModel ライフサイクルとの統合。
- **Android `@HiltViewModel` wrapper 削除**: `AndroidLoginViewModel` (52 行) と `AndroidHomeViewModel` (52 行) を削除。

---

## 移行後のアーキテクチャ

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
IosAppGraph (@DependencyGraph, シングルトン)
  ├── val loginViewModel: LoginViewModel
  ├── val homeViewModelFactory: HomeViewModel.Factory
  ├── @Provides fun provideIoDispatcher()
  └── @Provides fun provideDataStore()

トップレベル関数 (Swift 向け):
  ├── bootstrapIosAppGraph()    → ShowcaseApp.init
  ├── getLoginViewModel()       → LoginView
  └── getHomeViewModel(...)     → HomeView
```

---

## ファイル変更サマリ

### 削除

| ファイル | 理由 |
|---|---|
| `AndroidLoginViewModel.kt` | Metro `@Inject` の `LoginViewModel` で置換 |
| `AndroidHomeViewModel.kt` | Metro `@AssistedInject` の `HomeViewModel` で置換 |
| `DispatcherProvider.kt` | commonMain の `@IoDispatcher` qualifier で置換 |
| `TestDispatcherProvider.kt` | テストで `StandardTestDispatcher` を直接渡す形に |
| `ActivityLauncher.kt` | Single-Activity 化 |
| `ActivityLauncherImpl.kt` | Single-Activity 化 |
| `ActivityLauncherModule.kt` | Single-Activity 化 + Hilt 削除 |
| `InfoActivity.kt` | Single-Activity 化 |
| `PreferenceModule.kt` (Hilt) | Metro が graph 経由で DataStore を提供 |
| `RepositoryModule.kt` (Hilt) | Metro が `@Inject` で自動解決 |
| `hilt.gradle.kts` | `metro.gradle.kts` convention plugin で置換 |
| `KoinBootstrap.kt` | `IosAppGraph.kt` で置換 |
| `CoreKoinBridgeIos.kt` | `IosAppGraph.kt` で置換 |
| `CoreDataKoinModule.kt` | `IosAppGraph.kt` で置換 |
| `FeatureLoginKoinModule.kt` | `IosAppGraph.kt` で置換 |
| `FeatureHomeKoinModule.kt` | `IosAppGraph.kt` で置換 |
| `ShowcaseViewModelFactory.kt` | direct accessor パターンで multibinding を置換 |
| `AndroidDataStoreHolder.kt` | graph provider にインライン化 |

### 新規

| ファイル | 目的 |
|---|---|
| `ShowcaseAppGraph.kt` | Android Metro `@DependencyGraph` |
| `IosAppGraph.kt` | iOS Metro `@DependencyGraph` + Swift 向けヘルパー |
| `IoDispatcher.kt` | commonMain の Metro `@Qualifier` |
| `InfoRoute.kt` | Single-Activity 用の Compose Navigation route |
| `metro.gradle.kts` | Metro 用 convention plugin |

---

## iOS ビルド時の注意事項

Kotlin 側を変更した後に iOS アプリをビルドする際、以下の手順が必要です。

### 1. Xcode ビルド前に KMP framework を再ビルド

```bash
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64
```

Xcode は Kotlin framework を**自動的に再ビルドしません**。このステップを飛ばすと、Xcode は古い `.framework` に対してコンパイルし、`No such module 'ShowcaseKit'` やシンボル未解決エラーが発生します。

### 2. xcodebuild では具体的な Simulator を指定 (Apple Silicon)

```bash
# 利用可能な Simulator を一覧
xcrun simctl list devices available | grep iPhone

# 特定デバイスを指定してビルド (arm64 のみ)
xcodebuild -project iosApp/iosApp.xcodeproj \
  -scheme iosApp -configuration Debug \
  -destination 'platform=iOS Simulator,name=iPhone 16' \
  ONLY_ACTIVE_ARCH=YES build
```

Apple Silicon 上で `-destination 'generic/platform=iOS Simulator'` を使うと **arm64 と x86_64 の両方** をビルドしようとします。KMP framework は `iosSimulatorArm64` のみビルドされているため、x86_64 slice で `module file is incompatible with this Swift compiler` エラーが発生します。必ず具体的な Simulator デバイスを指定するか、`ONLY_ACTIVE_ARCH=YES` を追加してください。

### 3. ブランチ切り替え時は Xcode キャッシュをクリア

KMP framework の API が変更されるブランチ間で rebase やブランチ切り替えを行った後 (例: Kotlin の top-level 関数名変更)、Xcode の DerivedData が古い状態のまま残ることがあります:

```bash
# 方法A: Xcode で Clean Build Folder (Cmd+Shift+K)
# 方法B: DerivedData を削除
rm -rf ~/Library/Developer/Xcode/DerivedData/iosApp-*
```

### 4. Git worktree と Xcode

`git worktree` を使用する場合、Xcode プロジェクトは**メインリポジトリではなく worktree のディレクトリ**から開いてください。各 worktree は独自の `shared/build/` 出力を持ち、Xcode はプロジェクトの位置からの相対パスで framework を解決します。

---

## 参考

- [Metro GitHub](https://github.com/ZacSweers/metro)
- [DroidKaigi conference-app-2025](https://github.com/DroidKaigi/conference-app-2025) — Metro 採用の参照プロジェクト
- [Introducing Metro (Zac Sweers blog)](https://www.zacsweers.dev/introducing-metro/)
