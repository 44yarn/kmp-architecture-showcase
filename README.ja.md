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
- **AdaptiveString** — ローカライズリソースとリテラル文字列を単一の型で扱う抽象化 (下記参照)
- **DI** — Hilt (Android) + Koin (iOS)

### AdaptiveString: リソースとリテラル文字列の混在を扱う

`AdaptiveString` は `StringResource` (Compose Multiplatform Resources)
または プレーンな `String` のどちらも保持できる、カプセル化された単一クラス。
`DialogUiState` などの consumer は、内部がどちらの種類かを気にする必要がない。

**なぜ必要か。** 現実のプロジェクトでは、同じ UI フィールドに 2 種類の文字列源を
混在させる必要が頻繁にある:

- **既知のエラー型** → **ローカライズリソース** にマッピング (UI 層の責務)
  例: `is AuthException -> AdaptiveString(Res.string.login_invalid_credentials)`
- **未知のエラー or サーバー由来のテキスト** → **リテラル** としてそのまま扱う
  例: `AdaptiveString("予期せぬエラーが発生しました")`、または API レスポンスの
  `error_message` フィールドをそのまま表示するケース

統一型がないと、ダイアログ / スナックバー / エラー表示を組み立てるすべての箇所で
`String` と `StringResource` の分岐が必要になる。`AdaptiveString` は overloaded
constructor と `@Composable val value` (SwiftUI 向けには `async` な `resolve()`
拡張) で、この分岐を consumer から隠す。

**Showcase の実装箇所。** `feature/login` の `LoginViewModel.showLoginErrorDialog`
を参照。`AuthException` はローカライズリソースに、その他の throwable は
リテラルフォールバックにマッピングして、両方を同じ `DialogUiState.message`
フィールドに入れている。

**設計原則: `Exception.message` はログ用、UI には使わない。** `core/data` の
サンプル用 `AuthException` はロギング目的の診断メッセージを持つだけ。エラーから
ユーザー向けテキストへのマッピングは UI 層 (ViewModel) の責務で、例外の「型」に
応じて適切な `AdaptiveString` を選ぶ。`exception.message` を UI に表示するのは
アンチパターン。

**リソースの所属。** 文字列リソースは各 feature の
`feature/*/src/commonMain/composeResources/values/strings.xml` に配置する
(feature 固有の文字列をその feature 内に閉じる)。`core/ui-kit` には
`AdaptiveString` 型の定義だけを置く。

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
