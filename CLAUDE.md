# KMP Architecture Showcase

android-architecture-showcase の KMP 版。同じ画面構成 (Login / Home / Info) を Compose + SwiftUI で実装する。

## ビルド・テスト

- ビルド (Android): `./gradlew assembleDebug`
- テスト: `./gradlew testDebugUnitTest`
- フォーマット: `./gradlew spotlessApply`
- 静的解析: `./gradlew detekt`
- iOS フレームワーク: `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64`

## アーキテクチャ

- マルチモジュール KMP: `app → feature → core`（単方向依存）
- DI: Hilt (Android) + Koin (iOS)
- ViewModel: commonMain に共通 VM、androidMain に @HiltViewModel ラッパー
- Navigation: Android は @Serializable type-safe routes / iOS は SwiftUI NavigationStack
- iOS UI: SwiftUI（KMP ViewModel 参照）
- iOS フレームワーク: shared モジュールで umbrella framework (ShowcaseKit) を生成

## data 層の設計原則

- **Repository 実装は commonMain に置き、platform 固有 API に依存させない**。共通化が data 層の最優先事項
- platform 固有 API（DataStore のファイルパス解決、`NSFileManager`、`Context` など）は **DI 層（Hilt Module / Koin Module）でのみ参照**し、Repository に渡すインスタンスとして注入する。Repository 本体には漏らさない
- やむを得ず platform 固有実装にする場合（例: ストレージバックエンドそのものが本質的に異なる場合）は、**Why コメントで理由を明記**する
- **Hilt の `@Inject` を付けるためだけの薄い wrapper クラスは作らない**。commonMain クラスを Android で DI に流したい場合は、`@Binds` ではなく **`@Provides` で直接インスタンス化** する
- DataStore は KMP 版 (`androidx.datastore:datastore-preferences-core`) を使用し、`PreferenceDataStoreFactory.createWithPath` でパスのみ platform 側から注入する
- **単一実装の interface は切らない**。`Foo` + `FooImpl` のペアを「将来差し替えるかも」「テストのため」「クリーンアーキテクチャの作法だから」という理由だけで作るのは避ける。interface を切る正当な理由は次のいずれかが該当する時だけ:
  - **(a) モジュール境界での DIP (Dependency Inversion Principle)**: 依存の方向を逆転させる必要がある場合。例: `core/foundation` の `ActivityLauncher` interface と `app` モジュールの `ActivityLauncherImpl` — feature モジュール間の横依存を避けつつ、feature から他 feature の Activity を起動するための DIP 適用
  - **(b) 多相で扱う複数の実装が実際に存在する**: 例: `TypographyEntry` interface と 12 個の data object
  - **(c) platform 別に本質的に異なる実装が必要**: KMP プロジェクトで androidMain / iosMain で異なる API を呼ぶ場合
  - これらに該当しないなら単一 class で書き、必要になった時点で interface 化すれば良い。テスト用差し替えは多くの場合、より下層の依存（DI で渡すインスタンス）を差し替えるだけで足りる
- **型情報はキー側に閉じ込める**: `PreferenceKey<T : Any>` のように generic で型を運び、Storage 側は `get/put/observe/remove` の単一メソッドで全型を扱う。`getString`/`getBoolean` のような型ごとのメソッド分割は避ける

## KMP iOS 連携の注意事項

- **Kotlin の `init` prefix は Swift で `doInit` に変換される** — Swift の `init` は予約語のため、Kotlin/Native が自動で `do` prefix を付与する。iOS 向けの top-level 関数名には `init` を避け、`bootstrap` 等の動詞を使う
- **iOS framework は Kotlin 変更後に手動 rebuild が必要** — `./gradlew :shared:linkDebugFrameworkIosSimulatorArm64` を Xcode ビルド前に実行する。Xcode は Kotlin framework を自動再ビルドしない
- **Kotlin の sealed interface / enum 変更時は Swift 側の `switch` を確認** — Kotlin 側でメンバーを rename/削除した場合、Swift の `switch` 文が古い型名を参照してコンパイルエラーになる。`grep -r "旧名" iosApp/` で漏れを検出する

## 参照プロジェクト

- `~/apprica/android-architecture-showcase` — 元となる Android 版。UI 外観はこれに合わせる
- `~/apprica/habitune` — KMP パターン、Convention Plugin 構成、デザイントークンの参照元

## 新規モジュール作成時

1. `showcase.*` ファミリーのプラグインを適用
2. namespace: `io.github.yarn44.kmp.showcase.{module.path}`
3. `settings.gradle.kts` の include に追加
4. iOS 向けの場合は `shared/build.gradle.kts` の export/api に追加

## タスクワークフロー

`/task:research` → `/task:plan` → `/task:exec` の流れで実装を進める。

## グローバル Claude 設定（~/.claude/）との連携

- `~/.claude/` の設定は `~/mac-setup/` で一元管理している
- `.claude/` を変更・追加する際、同様の変更が `~/.claude/` にも必要か検討し、必要であれば提案する
