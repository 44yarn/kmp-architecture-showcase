# KMP DI Framework Comparison

[English](kmp-di-comparison.md)

KMP プロジェクトにおける DI フレームワークの比較調査メモ。
本リポジトリで実施した PoC と、Hilt/Dagger の KMP 対応状況を踏まえた記録。

> **訂正 (2026-08)。** 本ドキュメントの 2026-04 版は kotlin-inject-anvil を最も
> production-ready と結論づけた。その根拠の一つは、Metro PoC (#1) で cross-module
> `@ContributesTo` の集約が Android / iOS どちらでも動かなかったことだった。
> **これは Metro の制約ではなく、バージョン前提の不足だった。** Metro の multi-module
> aggregation は **Android / Apple とも Kotlin 2.3.20+ を要求する**が、PoC は Kotlin
> 2.3.10 で実施していた。Kotlin 2.4.10 + Metro 1.4.2 で再検証したところ、両プラット
> フォームで集約が動作する。詳細は [訂正後の検証結果](#訂正後の検証結果-2026-08) を参照。

## 比較対象

| Framework | Version | Type | Maintainer |
|-----------|---------|------|------------|
| Hilt (Dagger) | 2.57.2 | Annotation Processor / KSP | Google |
| Koin | 3.5.6 | Service Locator (runtime) | Arnaud Giuliani |
| Metro | 1.4.2 | Kotlin Compiler Plugin | Zac Sweers |
| kotlin-inject + anvil | 0.9.0 + 0.1.7 | KSP | Eva Tatarka / Ralf Wondratschek |

## 上流の状況 (2026-08)

2026-04 版からの最大の変化は、2 つのプロジェクトのメンテナンス状況が**逆転した**こと。

### kotlin-inject-anvil: maintenance mode 入り

[README](https://github.com/amzn/kotlin-inject-anvil) が 2026-07-15 に更新された:

> kotlin-inject-anvil is in maintenance mode. The last meaningful release was over a year
> ago. ... I'm happy to make critical bug fixes in case the KSP integration breaks, but
> nothing further.
>
> **We moved our projects from kotlin-inject-(anvil) to Metro**, which is a better
> solution long term.

所有権も Amazon から離れ、`vRallev/kotlin-inject-anvil` として個人メンテに移行した
（パッケージ名と Maven 座標は互換性のため `software.amazon.*` のまま）。

これにより、2026-04 版が kotlin-inject-anvil を選んだ根拠である「大規模 KMP での本番
実績」は無効化される。**その実績の当事者が Metro へ移った**ため。

`evant/kotlin-inject` 本体も **2026-01-07**（v0.9.0）以降コミットが無い。

### Metro: 1.0 stable 到達、活発に開発中

- **1.0.0 stable が 2026-04-27**（PoC #1 実施の 2 週間後）、1.4.2 が 2026-08-13。
- 安定保証の範囲は **runtime 系アーティファクトの ABI**（runtime / metrox / Gradle
  plugin）。コンパイラプラグイン自体は対象外で、Kotlin 互換はおよそ 5 バージョンの
  移動窓として
  [`docs/compatibility.md`](https://github.com/ZacSweers/metro/blob/main/docs/compatibility.md)
  に明記されている。
- kotlin-inject-anvil の作者が Metro の contributor に加わっている。

### Hilt (Dagger): 依然として未提供

Dagger チームの最新コメント
（[google/dagger#3916](https://github.com/google/dagger/issues/3916)、**2026-08-11**）:

> yes, this is still on the roadmap. The progress slowed/stopped due to multiple issues --
> first, the switch from KSP1 to KSP2, and then the shift to focus on KSP performance
> issues. However, the performance work is wrapping up and we are now in a position to get
> back to this soon.

日付の言及なし。2026-04 版の「早くても 2026 年後半〜2027 年」という見込みは、さらに
後ろ倒しと読むべき。

### Koin: コンパイラプラグイン化

[`InsertKoinIO/koin-compiler-plugin`](https://github.com/InsertKoinIO/koin-compiler-plugin)
が **1.1.0** に到達。Koin は KSP / アノテーション方式から、`single<T>()` をコンパイル時に
解決するネイティブ Kotlin コンパイラプラグインへ移行しつつある。従来の比較表にある
「Koin = コンパイル時検証なしの runtime service locator」という分類は既に古い。ただし
まだ新しく（star 100 未満）、本比較では評価対象外とした。

## 訂正後の検証結果 (2026-08)

同一 base から DI フレームワークのみを変数として 2 本の実験ブランチを作成し、両方とも
`assembleDebug` + `:shared:linkDebugFrameworkIosSimulatorArm64` の成功を確認した。

### cross-module aggregation は iOS で動作する

Metro の
[`docs/multiplatform.md`](https://github.com/ZacSweers/metro/blob/main/docs/multiplatform.md)
はターゲット別に Kotlin の下限を明記している:

| ターゲット | 必要な Kotlin |
|---|---|
| JVM | 2.3.0 |
| **Android** | **2.3.20** |
| **Apple** | **2.3.20** |
| JS | 2.3.21 |
| Wasm / Linux / Windows / Android Native | 2.3.20 |

PoC #1 は Kotlin **2.3.10** で実施しており、検証した両プラットフォームとも下限未満
だった。対応する上流 issue は 2 件とも close 済み:

- [ZacSweers/metro#460](https://github.com/ZacSweers/metro/issues/460) — 集約が
  jvm/android に限定される — 2026-01-20 close
- [ZacSweers/metro#1556](https://github.com/ZacSweers/metro/issues/1556) — native klib の
  シリアライズで qualifier アノテーションが落ちる — 2026-01-26 close

Kotlin 2.4.10 + Metro 1.4.2 では、iOS グラフに `@Provides` を **1 つも書かず**、6 個の
provider すべてが他モジュールの `@ContributesTo` から集約される（Android グラフと同一）。

### 本コードベースでの実測

| 指標 | kotlin-inject-anvil | Metro 1.4.2 | 差 |
|---|---|---|---|
| DI 宣言コード | 276 行 | 201 行 | **−75 行 (−27%)** |
| iOS グラフ（単一ファイル） | 105 行 | 41 行 | −64 |
| ターゲット別 `expect/actual` ファクトリ | 3 ファイル | 0 | `createGraph<T>()` で代替 |
| Gradle の DI 配線 | 69 行 | 13 行 | −56 |
| 各モジュールの DI 配線 | 9 行 | 1 行 | plugin id のみ |
| 全体差分 | — | — | **+72 / −272** |

移行コストは小さい。各モジュールの contribution interface はアノテーション面が同一なので
**import の差し替えのみ**で済む。実質的な書き換えが必要だったのは 3 箇所だけ — Android
グラフ（`@MergeComponent` + コンストラクタ引数 → `@DependencyGraph` +
`@DependencyGraph.Factory`）、iOS グラフ、assisted injection の ViewModel における
`@Inject` → `@AssistedInject`。

### ビルド速度: この規模では差がない

clean ビルド、ビルドキャッシュ無効、各 3 回:

| | 1 回目 | 2 回目 | 3 回目 | 平均 |
|---|---|---|---|---|
| kotlin-inject (KSP) | 18s | 16s | 17s | **17.0s** |
| Metro | 16s | 17s | 18s | **17.0s** |

Metro が公称する 50〜80% のビルド改善は、**約 3,200 行の本プロジェクトでは観測できない**。
この規模でビルド速度を移行理由に挙げるのは実測に反する。

### iOS の fallback は死んだコードを生んでいた

iOS グラフが素の `@Component`（merge なし）だったため、`@ContributesTo` を付けた
`IosDataStoreComponent` は到達不能だった — 41 行、参照ゼロ。その
`providePreferencesPath()` の NSDocumentDirectory ロジックは 2 箇所に存在していた
（グラフ内にインライン展開された生きたコピーと、モジュール側の死んだコピー）。集約が
効けば 1 本化される。これが「全 provider をインライン展開する」fallback の具体的な
保守コスト。

## ツールチェーン側の制約（DI 選択とは独立）

実験ブランチのビルド中に判明したもの。DI フレームワークの選択に関わらず適用される。

- **KSP は 2.3.6 に固定される。** KSP 2.3.7 が Kotlin target language version を 2.3 に
  引き上げたため、`gradle-conventions` の precompiled script plugin ビルド（Gradle
  8.14.3、埋め込み Kotlin 2.0）が読めない。Metro は KSP 自体が不要なので、この固定が
  消える。
- **`iosX64` の削除が必須。** Compose Multiplatform 1.11.0 以降が Apple x86_64 の
  成果物を提供しない（[KT-81596](https://youtrack.jetbrains.com/issue/KT-81596)）。この
  1 ターゲット削除に、kotlin-inject 側は 6 モジュールの `kspIosX64` 配線の個別修正が
  必要だった。Metro 側には該当配線が無い。
- **kotlin-inject は Kotlin 2.4.10 で動作する。** ビルド成功を確認済み。上流の停滞は
  ビルドの破綻とは別物であり、移行を強制する事象は発生していない。
- **Metro の Gradle プラグインは precompiled script plugin に包めない**（Kotlin
  プラグイン適用済みが前提のため）。各モジュールの `plugins {}` で直接適用する。

## 判断基準

### Metro を選ぶべきケース

- 今すぐ KMP で DI を統一したい（Apple ターゲットの cross-module 集約を含む）
- **Kotlin 2.3.20+** を使える（JVM のみなら 2.3.0+）
- ビルドから KSP を完全に外したい
- コンパイラプラグインの互換が移動窓であることを許容できる（Kotlin 更新時に Metro の
  追従バージョン上げが要る場合がある）

### kotlin-inject-anvil を選ぶべきケース

- 既に採用済みで、移行の切迫理由が無い — 現行 Kotlin でも問題なくビルドできる
- Kotlin < 2.3.20 に留まる必要があり、かつ Android の cross-module 集約が要る
- コンパイラプラグインより KSP の保守的な互換方針を好む

ただし maintenance mode かつ上流作者自身が Metro を推奨している以上、**新規採用の
正当化は難しい**。

### Hilt+Koin を維持すべきケース

- KMP 化予定の無い Android 単体プロジェクト
- 二重実装のコストが無視できる規模

## 本プロジェクトの結論

[#4](https://github.com/44yarn/kmp-architecture-showcase/issues/4) で **Metro に移行した**。
理由は上流の健全性と構造的な重複の解消であり、**ビルド速度ではない**:

1. kotlin-inject-anvil が maintenance mode で、作者自身が Metro を推奨している
2. cross-module 集約が iOS で動くため、provider インライン展開の fallback とそれが生んだ
   死んだコードが解消される
3. KSP がビルドから完全に消え、モジュール別・ターゲット別の配線も不要になる
4. Metro は 1.0 stable かつリリースが活発

## 参考リンク

### PoC レポート（各ブランチ）
- [Metro PoC Report (EN)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/metro-di-poc/docs/metro-migration.md)
- [Metro PoC レポート (JA)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/metro-di-poc/docs/metro-migration-ja.md)
- [kotlin-inject PoC Report (EN)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/kotlin-inject-poc/docs/kotlin-inject-migration.md)
- [kotlin-inject PoC レポート (JA)](https://github.com/44yarn/kmp-architecture-showcase/blob/feature/kotlin-inject-poc/docs/kotlin-inject-migration-ja.md)

### 公式リソース
- [ZacSweers/metro](https://github.com/ZacSweers/metro) — [compatibility](https://github.com/ZacSweers/metro/blob/main/docs/compatibility.md) / [multiplatform](https://github.com/ZacSweers/metro/blob/main/docs/multiplatform.md)
- [Metro is stable (Zac Sweers)](https://www.zacsweers.dev/metro-is-stable/)
- [amzn/kotlin-inject-anvil](https://github.com/amzn/kotlin-inject-anvil) — maintenance mode の告知
- [evant/kotlin-inject](https://github.com/evant/kotlin-inject)
- [google/dagger — Issue #3916 (KMP)](https://github.com/google/dagger/issues/3916)
- [google/dagger — Issue #4834 (Hilt + Android-KMP plugin)](https://github.com/google/dagger/issues/4834)
- [InsertKoinIO/koin-compiler-plugin](https://github.com/InsertKoinIO/koin-compiler-plugin)

### 採用事例・記事
- [Introducing kotlin-inject-anvil (Ralf Wondratschek)](https://ralf-wondratschek.com/blog/introducing-kotlin-inject-anvil)
- [Introducing Metro (Zac Sweers)](https://www.zacsweers.dev/introducing-metro/)
- [tv-maniac kotlin-inject-anvil integration (ProAndroidDev)](https://proandroiddev.com/integrate-kotlin-inject-anvil-to-tv-maniac-e1330c9cb566)
- [Dagger/Hilt → kotlin-inject migration (ProAndroidDev)](https://proandroiddev.com/from-dagger-hilt-into-the-multiplatform-world-with-kotlin-inject-647d8e3bddd5)

---

*Last updated: 2026-08-22*
