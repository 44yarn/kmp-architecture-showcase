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
- **AdaptiveString / AdaptiveImage** — ローカライズリソースとリテラル文字列 / リモート URL を単一の型で扱う抽象化 (下記参照)
- **DI 境界** — Repository / use-case 類は `commonMain` に置く。platform 固有 API (DataStore のファイルパス、`Context`、`NSFileManager` など) は DI モジュール (Hilt `@Provides` / Koin `module`) からのみ参照し、依存として注入する。`@Inject` を付けるためだけの薄い Hilt wrapper クラスは作らず、`@Provides` で commonMain クラスを直接インスタンス化する
- **Stateless content split** — Android の各画面は、ViewModel state を collect して effect を配線する stateful な `XxxScreen` と、`uiState` + `actions` だけを受け取る stateless な `XxxContent` のペアで構成する。`@Preview` が描画するのは後者なので、プレビューは DI や coroutine に触れない
- **Lifecycle-aware effect 収集** — 一過性 effect の channel は `Flow<T>.CollectAsEffect` (`core/foundation`) で collect する。中で `repeatOnLifecycle(STARTED)` をラップしているので、画面が background の間に effect が配信されない
- **DI** — Hilt (Android) + Koin (iOS)

### Adaptive types: リソースとランタイム値の混在を扱う

`core/ui-kit` には `AdaptiveString` と `AdaptiveImage` という、同じ
設計思想を持つ 2 つの型がある。それぞれ、**カプセル化された単一クラス**で
Compose Multiplatform のローカライズリソースとランタイム値 (リテラル
文字列 / リモート URL) を単一の consumer 向け型に統合する。

**なぜ必要か。** 現実のプロジェクトでは、同じ UI フィールドに 2 種類の
文字列 / 画像源を混在させる必要がある:

- **既知の状態・ローカルアセット** → **ローカライズリソース** にマッピング
  (UI 層の責務)
  例: `is AuthException -> AdaptiveString(Res.string.login_invalid_credentials)`
- **サーバー由来のテキスト / リモート画像** → **リテラルまたは URL**
  例: `AdaptiveString("予期せぬエラーが発生しました")`、
  `AdaptiveImage("https://example.com/avatar.png")`

統一型がないと、ダイアログ / スナックバー / 画像スロットを組み立てる
すべての箇所で `String` と `StringResource`、または URL と
`DrawableResource` の分岐が必要になる。これらの型は overloaded
constructor と単一の resolve パスでこの分岐を consumer から隠すので、
ViewModel はどちらの形でも同じ型として emit でき、UI 層はそのまま
描画できる。

両方とも同じカプセル化パターンを採用している: **private な primary
constructor** が内部の `val` フィールドを保持し、**複数の secondary
constructor** が有効な組み合わせだけを公開する。呼び出し側が不正な
ハイブリッド形を構築できない仕組み。

#### `AdaptiveString`

プレーンなリテラル `String` か、ローカライズされた `StringResource`
(+ オプションのフォーマット引数) を保持する。Android Compose 向けの
`@Composable val value: String` アクセサと、SwiftUI 向けに `iosMain`
で提供される `suspend fun resolve(): String` 拡張を持つ — SKIE が
これを Swift の `async throws` にブリッジする。

**Showcase の実装箇所。** `feature/login` の
`LoginViewModel.showLoginErrorDialog` を参照。`AuthException` は
ローカライズリソースに、その他の throwable はリテラルフォールバックに
マッピングして、両方を同じ `DialogUiState.message` フィールドに入れている。

**設計原則: `Exception.message` はログ用、UI には使わない。**
`core/data` のサンプル用 `AuthException` はロギング目的の診断メッセージを
持つだけ。エラーからユーザー向けテキストへのマッピングは UI 層 (ViewModel)
の責務で、例外の「型」に応じて適切な `AdaptiveString` を選ぶ。
`exception.message` を UI に表示するのはアンチパターン。

#### `AdaptiveImage`

リモート画像の URL か、ローカルの `DrawableResource` のどちらかを保持し、
加えて描画方式を表す `ImageType` を持つ:

- `ImageType.Icon` — 小さく正方形のアイコン風
- `ImageType.FillMaxWidth(contentScale, aspectRatio)` — コンテナの
  幅いっぱいに、固定アスペクト比で広げる

描画 (rendering) は意図的に呼び出し側に委ねている。Composable 側で
`type` に応じて `AsyncImage(url)` と `painterResource(resource)` を
切り替えれば良い。

`AdaptiveImage` は本 showcase のどの画面からもまだ使われていない。
アバターやサムネイル、ヘッダー画像でリモートとローカルを混在させたい
時にすぐ使える pattern として配置してある。

#### リソースの所属

文字列リソースも drawable リソースも、各 feature の
`src/commonMain/composeResources/` 配下 (文字列は `values/strings.xml`、
画像は `drawable/`) に配置する。feature 固有のリソースは feature 内に
閉じる。`core/ui-kit` には `AdaptiveString` / `AdaptiveImage` の型定義
だけを置き、リソースファイルは置かない。

## モジュール構成

```
gradle-conventions       Convention Plugins（ビルド設定の共通化）
app                      Android アプリ本体、NavGraph、Hilt セットアップ
shared                   iOS 向け umbrella framework (ShowcaseKit)
+-- core
|   +-- foundation       KmpViewModel、Result 拡張
|   +-- ui-kit           DialogPresenter、SnackbarPresenter、IndicatorState、AppTheme、AdaptiveString、AdaptiveImage
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

### iOS + Compose Multiplatform Resources

Compose Multiplatform Resources (`StringResource`, `DrawableResource` 等)
は Compose Multiplatform UI を前提にした仕組み。iOS 側を SwiftUI で
構築している場合 — このプロジェクトのように — `.cvr` ファイルは
Kotlin フレームワークにはコンパイルされるが、**iOS アプリバンドルには
自動コピーされない**。そのため Swift 側で最初に `StringResource` を
resolve した時点で、ランタイムに `MissingResourceException` が発生する。

本プロジェクトは、Xcode の Run Script Build Phase
(`script/sync-compose-resources.sh`、`iosApp/project.yml` で設定) で
このギャップを埋めている。スクリプトは `composeResources/` を持つ
各 feature モジュールに対して `assemble<Target>MainResources` Gradle
タスクを実行し、結果を `.app` バンドルに rsync して Compose Resources
ランタイムが期待するレイアウトに配置する。

独自の `composeResources/` ディレクトリを持つ feature モジュールを
新規追加した場合は、`script/sync-compose-resources.sh` の `MODULES`
配列に Gradle パスを追加すること。

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
