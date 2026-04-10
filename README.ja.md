# KMP Architecture Showcase

[English](README.md)

Kotlin Multiplatform (KMP) アーキテクチャパターンを実演するサンプルアプリケーション。
同じ Login / Home / Info 画面を Jetpack Compose (Android) と SwiftUI (iOS) で実装し、
ViewModel、リポジトリ、デザイントークンを共通モジュールで共有する。

[android-architecture-showcase](https://github.com/44yarn/android-architecture-showcase) の KMP 版。

## 画面構成

```
Login --- Login 成功 -----------> Home（"Welcome, {name}!" Snackbar）
  |                                PreferenceStorage デモ
  |
  +--- Login 失敗 -> ErrorDialog（DialogPresenter: commonMain）
  |         +- Cancel -> 閉じる
  |         +- Guest Login -> Home（"Guest mode" Snackbar）
  |
  +--- Information -> Info 画面
```

## showcase するパターン

| 画面 | モジュール | パターン |
|------|-----------|----------|
| Login | feature:login | DialogPresenter (commonMain)、IndicatorState、Effect（画面遷移） |
| Home | feature:home | SnackbarPresenter、PreferenceStorage、BackHandler (Android) |
| Info | feature:info | 静的コンテンツ表示 |

### UI フィードバック 3パターン

| パターン | 用途 | 仕組み |
|----------|------|--------|
| Effect | fire-and-forget（画面遷移） | `Channel<Effect>` + `receiveAsFlow()` |
| Dialog | ユーザー応答を待つ | `DialogPresenter` + `suspendCancellableCoroutine`（commonMain） |
| Snackbar | 即時表示 | `SnackbarPresenter.show()` 直接呼び出し |

### クロスプラットフォーム デザインシステム

| レイヤー | 配置 | 説明 |
|---------|------|------|
| デザイントークン生値 | `commonMain` | `AppColorValues`, `AppTypographyValues`, `AppSpacingValues`, `AppCornerRadiusValues`（プラットフォーム非依存） |
| Compose ラッパー | `commonMain` | `AppColorToken`, `AppTypography`, `AppSpacing`, `CornerRadius` |
| Material3 マッピング | `androidMain` | `AppColorScheme`, `AppThemeProvider` |
| SwiftUI ラッパー | `iosApp/Theme/` | `AppColors`, `AppFonts`, `AppSpacings`, `AppCornerRadii` |

### その他の設計パターン

- **KMP ViewModel** — `commonMain` に共通 ViewModel、`androidMain` に `@HiltViewModel` ラッパー
- **SKIE** — Kotlin `StateFlow` / `Flow` を Swift `AsyncSequence` に自動変換
- **Actions クラス** — コールバックを data class に集約
- **Convention Plugin** — gradle-conventions でビルド設定を共通化
- **PreferenceKey / PreferenceStorage** — Preferences DataStore (KMP) の型安全ラッパー。値の型をキー側に閉じ込める設計
- **DI** — Hilt (Android) + Koin (iOS)

## モジュール構成

```
gradle-conventions       Convention Plugins（ビルド設定の共通化）
app                      Android アプリ本体、NavGraph、Hilt セットアップ
shared                   iOS 向け umbrella framework (ShowcaseKit)
+-- core
|   +-- foundation       KmpViewModel、Result 拡張
|   +-- ui-kit           DialogPresenter、SnackbarPresenter、IndicatorState、AppTheme
|   +-- data             AuthRepository、PreferenceStorage（DataStore KMP）
+-- feature
|   +-- login            ログイン画面
|   +-- home             ホーム画面
|   +-- info             情報画面
iosApp                   iOS アプリ（SwiftUI + XcodeGen）
```

依存方向: `app/iosApp -> feature -> core`（一方向）。

## Convention Plugins（gradle-conventions）

| プラグイン | 役割 |
|-----------|------|
| showcase.convention.app | Application モジュール設定 |
| showcase.convention.kmp-feature | KMP feature モジュール（Compose + SKIE） |
| showcase.convention.kmp-module | KMP core / library モジュールのベースプラグイン |
| showcase.convention.kmp-sqldelight | KMP モジュールに SQLDelight を追加 |
| showcase.primitive.hilt | Hilt DI + KSP |
| showcase.primitive.spotless | コードフォーマット |
| showcase.primitive.detekt | 静的解析 |

## 技術スタック

| カテゴリ | ライブラリ |
|----------|-----------|
| 言語 | Kotlin 2.3 / Swift 5.9 |
| UI | Jetpack Compose (Android) / SwiftUI (iOS) |
| DI | Hilt (Android) / Koin (iOS) |
| Navigation | Navigation Compose (Android) / NavigationStack (iOS) |
| 非同期 | Kotlin Coroutines + Flow / SKIE AsyncSequence |
| データ保存 | Preferences DataStore (KMP) |
| ビルド | AGP 8.13、KSP、Convention Plugins、XcodeGen |
| コード品質 | Spotless、detekt、SwiftFormat、SwiftLint |

## 動作要件

- JDK 21
- Android: minSdk 31 / compileSdk 36 / targetSdk 36
- iOS: 17.0+
- Xcode 16+
- [Mint](https://github.com/yonaskolb/Mint)（SwiftFormat / SwiftLint 用）
- [XcodeGen](https://github.com/yonaskolb/XcodeGen)（Xcode プロジェクト生成用）

## ビルド

```bash
# Android デバッグビルド
./gradlew assembleDebug

# テスト
./gradlew testDebugUnitTest

# コードフォーマット（Kotlin）
./gradlew spotlessApply

# 静的解析
./gradlew detekt

# Swift フォーマット
./script/format-swift.sh

# iOS フレームワーク
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64

# 全ステップ一括実行（上記に加えて iOS ビルドまで走る）
./script/preflight.sh
```

## ライセンス

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
