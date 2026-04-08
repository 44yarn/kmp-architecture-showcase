#!/usr/bin/env bash

set -Eeuo pipefail

# Colors and styles
if [[ -t 1 ]]; then
  BOLD="\033[1m"; DIM="\033[2m"; RED="\033[31m"; GREEN="\033[32m"; YELLOW="\033[33m"; BLUE="\033[34m"; MAGENTA="\033[35m"; CYAN="\033[36m"; RESET="\033[0m"
else
  BOLD=""; DIM=""; RED=""; GREEN=""; YELLOW=""; BLUE=""; MAGENTA=""; CYAN=""; RESET=""
fi

hr() {
  printf "${DIM}%s${RESET}\n" "──────────────────────────────────────────────────────────────────────────────"
}

section() {
  local title="$1"
  hr
  printf "${BOLD}✨ %s${RESET}\n" "$title"
  hr
}

success() { printf "${GREEN}✅ %s${RESET}\n" "$1"; }
fail()    { printf "${RED}❌ %s${RESET}\n" "$1"; }
note()    { printf "${CYAN}ℹ️  %s${RESET}\n" "$1"; }

usage() {
  cat <<USAGE
使い方: $(basename "$0") [オプション]

オプション:
  -f, --from               対話選択で「開始ステップ」を1つ選び、そこから最後まで実行
  -s, --single             対話選択で1ステップのみ実行（fzf優先、なければ番号入力）
  -m, --multi              対話選択で複数ステップ実行（定義順で実行）
  -h, --help               このヘルプを表示

iOSビルドのログ出力:
  IOS_XCODE_VERBOSE=true   xcodebuild の詳細出力（デフォルトは SUCCEEDED/FAILED の簡潔表示）

主な環境変数（iOS）:
  IOS_XCODEPROJECT  (既定: iosApp/iosApp.xcodeproj)
  IOS_SCHEME        (既定: iosApp)
  IOS_CONFIGURATION (既定: Debug)
  IOS_SDK           (既定: iphonesimulator)
  IOS_DESTINATION   (既定: platform=iOS Simulator,name=\"iPhone 16\")
  IOS_SIM_NAME      (簡易にシミュレータ名だけ変える場合)
  IOS_CLEAN_BUILD   true で clean build を実行（遅い）

補足:
  - -f/-s/-m の対話オプションは fzf が必須です（非TTY/未導入の場合は実行を中止します）。
  - 複数選択時も実行順は定義順を維持します。
USAGE
}

# iOS build defaults (override via env)
IOS_XCODEPROJECT="${IOS_XCODEPROJECT:-iosApp/iosApp.xcodeproj}"
IOS_SCHEME="${IOS_SCHEME:-iosApp}"
IOS_CONFIGURATION="${IOS_CONFIGURATION:-Debug}"
IOS_SDK="${IOS_SDK:-iphonesimulator}"
IOS_DESTINATION="${IOS_DESTINATION:-platform=iOS Simulator,name=${IOS_SIM_NAME:-iPhone 16}}"
IOS_CLEAN_BUILD="${IOS_CLEAN_BUILD:-false}"

# Find repo root by locating gradlew upward from this script's directory
find_repo_root() {
  local dir
  dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
  while [[ "$dir" != "/" ]]; do
    if [[ -f "$dir/gradlew" ]]; then
      echo "$dir"
      return 0
    fi
    dir="$(dirname -- "$dir")"
  done
  return 1
}

main() {
  local start_ts
  start_ts=$(date +%s)

  local repo_root
  if ! repo_root="$(find_repo_root)"; then
    fail "リポジトリルート(gradlew)が見つかりませんでした。スクリプトの配置場所を確認してください。"
    exit 1
  fi

  cd "$repo_root"
  section "Showcase Dev パイプラインを開始 🚀"
  note "リポジトリルート: $(pwd)"

  # Steps: description, command string, and optional failure hints
  local -a DESCS=()
  local -a CMDS=()
  local -a HINTS=()

  DESCS+=("🧹 Kotlin を整形: spotlessApply"); CMDS+=("./gradlew spotlessApply"); HINTS+=("")

  DESCS+=("🔍 静的解析: detekt"); CMDS+=("./gradlew detekt"); HINTS+=("")

  local format_swift_hint=""
  if [[ ! -f "./script/format-swift.sh" ]]; then
    format_swift_hint="format-swift.sh が見つかりません。swiftformat がインストール済みか確認してください（例: brew install swiftformat）。その後、format-swift.sh をリポジトリルートに配置してください。"
  fi
  DESCS+=("🧼 Swift を整形: format-swift.sh"); CMDS+=("./script/format-swift.sh"); HINTS+=("$format_swift_hint")

  DESCS+=("🧩 iOS KMP リンク: :shared:linkDebugFrameworkIosSimulatorArm64"); CMDS+=("./gradlew :shared:linkDebugFrameworkIosSimulatorArm64"); HINTS+=("")

  DESCS+=("📦 APK ビルド: assembleDebug"); CMDS+=("./gradlew assembleDebug"); HINTS+=("")

  DESCS+=("🧪 ユニットテスト: testDebugUnitTest"); CMDS+=("./gradlew testDebugUnitTest"); HINTS+=("")

  # Optional: iOS build via xcodebuild on macOS
  if [[ "$(uname -s)" == "Darwin" ]] && command -v xcodebuild >/dev/null 2>&1; then
    local XCODE_BASE_CMD
    if [[ "$IOS_CLEAN_BUILD" == "true" ]]; then
      DESCS+=("🍏 iOSクリーンビルド: $IOS_SCHEME ($IOS_CONFIGURATION)")
      XCODE_BASE_CMD="xcodebuild -project $IOS_XCODEPROJECT -scheme $IOS_SCHEME -configuration $IOS_CONFIGURATION -sdk $IOS_SDK -destination '$IOS_DESTINATION' clean build"
    else
      DESCS+=("🍏 iOSビルド: $IOS_SCHEME ($IOS_CONFIGURATION)")
      XCODE_BASE_CMD="xcodebuild -project $IOS_XCODEPROJECT -scheme $IOS_SCHEME -configuration $IOS_CONFIGURATION -sdk $IOS_SDK -destination '$IOS_DESTINATION' build"
    fi
    if [[ "${IOS_XCODE_VERBOSE:-}" == "true" ]]; then
      CMDS+=("$XCODE_BASE_CMD")
      HINTS+=("出力は詳細表示です。簡潔表示に戻すには IOS_XCODE_VERBOSE を未設定にしてください。")
    else
      CMDS+=("$XCODE_BASE_CMD 2>&1 | grep -E '(SUCCEEDED|FAILED)'")
      HINTS+=("エラー詳細を確認するには以下を実行してください: $XCODE_BASE_CMD")
    fi
  else
    note "macOS ではないか xcodebuild が見つからないため iOS ビルドはスキップします。"
  fi

  # ---- Selection options -------------------------------------------------
  has_fzf() { command -v fzf >/dev/null 2>&1; }
  print_steps() {
    local n=${#DESCS[@]}
    local idx
    for ((idx=0; idx<n; idx++)); do
      printf "%2d: %s\n" "$((idx+1))" "${DESCS[$idx]}"
    done
  }

  pick_one_idx() {
    if ! has_fzf || { [[ ! -t 1 ]] && [[ ! -t 0 ]]; }; then
      fail "対話選択には fzf が必要です。インストールしてください（例: brew install fzf）。"
      exit 2
    fi
    local chosen=""
    chosen=$(print_steps | fzf --ansi --height 60% --prompt="開始ステップ > " --border | awk -F: '{print $1}' | xargs)
    if [[ "$chosen" =~ ^[0-9]+$ ]]; then
      local id=$((chosen-1))
      if (( id>=0 && id<${#DESCS[@]} )); then echo "$id"; return 0; fi
    fi
    echo 0
  }

  # Parse args
  local opt_from_interactive=false
  local opt_mode="all"
  while [[ $# -gt 0 ]]; do
    case "$1" in
      -h|--help)
        usage; exit 0;;
      -f|--from)
        opt_from_interactive=true; shift;;
      -s|--single)
        opt_mode="single"; shift;;
      -m|--multi)
        opt_mode="multi"; shift;;
      *)
        shift;;
    esac
  done

  # Build EXEC list of indices (0-based)
  local -a EXEC=()
  local total=${#CMDS[@]}
  local pick_ids=""
  local selection_used=false

  if [[ "$opt_mode" == "single" || "$opt_mode" == "multi" ]]; then
    selection_used=true
    if ! has_fzf || { [[ ! -t 1 ]] && [[ ! -t 0 ]]; }; then
      fail "対話選択には fzf が必要です。インストールしてください（例: brew install fzf）。"
      exit 2
    fi
    local fzf_opts=(--ansi --height 60% --prompt="ステップ選択 > " --border)
    [[ "$opt_mode" == "multi" ]] && fzf_opts+=(--multi)
    pick_ids=$(print_steps | fzf "${fzf_opts[@]}" | awk -F: '{print $1}' | xargs)
    for tok in ${pick_ids//,/ }; do
      if [[ "$tok" =~ ^[0-9]+$ ]]; then
        local id=$((tok-1))
        if (( id>=0 && id<total )); then EXEC+=($id); fi
      fi
    done
  elif [[ "$opt_from_interactive" == true ]]; then
    selection_used=true
    local start_idx
    start_idx=$(pick_one_idx)
    if (( start_idx<0 )); then start_idx=0; fi
    local k
    for ((k=start_idx;k<total;k++)); do EXEC+=($k); done
  else
    local k
    for ((k=0;k<total;k++)); do EXEC+=($k); done
  fi

  if [[ ${#EXEC[@]} -eq 0 ]]; then
    local k
    for ((k=0;k<total;k++)); do EXEC+=($k); done
  fi

  local i
  for i in "${EXEC[@]}"; do
    local step=$((i+1))
    local desc="${DESCS[$i]}"
    local cmd="${CMDS[$i]}"

    printf "\n${BOLD}▶️  Step %d/%d${RESET}: %s\n" "$step" "$total" "$desc"
    printf "${DIM}$ %s${RESET}\n" "$cmd"
    hr

    local step_start
    step_start=$(date +%s)

    if bash -c "$cmd"; then
      local step_end
      step_end=$(date +%s)
      local elapsed=$((step_end - step_start))
      success "完了 (step ${step}/${total}) ⏱ ${elapsed}s"
    else
      local rc=$?
      hr
      fail "失敗 (step ${step}/${total}) — コマンド: $cmd"
      note "途中で停止しました。上のログを確認してください。終了コード: $rc"
      if [[ -n "${HINTS[$i]:-}" ]]; then
        note "${HINTS[$i]}"
      fi
      exit "$rc"
    fi
  done

  local end_ts
  end_ts=$(date +%s)
  local total_elapsed=$((end_ts - start_ts))
  section "🎉 すべて完了！"
  if [[ "$selection_used" == true || ${#EXEC[@]} -ne $total ]]; then
    success "選択ステップ ${#EXEC[@]} 件成功。合計時間: ${total_elapsed}s"
  else
    success "全 ${total} ステップ成功。合計時間: ${total_elapsed}s"
  fi
}

main "$@"
