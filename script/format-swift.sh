#!/bin/bash

# プロジェクトルートに移動（script/ 配下から実行されても動作するように）
cd "$(dirname "$0")/.." || exit 1

echo "📱 Formatting Swift code..."

# Mintでツールをインストール（まだの場合）
cd iosApp && mint bootstrap && cd ..

# SwiftFormatを実行
echo "Running SwiftFormat on iosApp..."
cd iosApp && mint run swiftformat . && cd ..

# shared/src/iosMainにSwiftファイルがあればフォーマット
if [ -d "shared/src/iosMain/swift" ]; then
    echo "Running SwiftFormat on shared/src/iosMain/swift..."
    cd iosApp && mint run swiftformat ../shared/src/iosMain/swift && cd ..
fi

# SwiftLintで自動修正
echo "Running SwiftLint autocorrect on iosApp..."
cd iosApp && mint run swiftlint --fix --config ../.swiftlint.yml . && cd ..

if [ -d "shared/src/iosMain/swift" ]; then
    echo "Running SwiftLint on shared/src/iosMain/swift..."
    cd iosApp && mint run swiftlint --fix --config ../.swiftlint.yml ../shared/src/iosMain/swift && cd ..
fi

# SwiftLintでチェック
echo "Running SwiftLint check..."
cd iosApp && mint run swiftlint lint --config ../.swiftlint.yml . && cd ..

if [ -d "shared/src/iosMain/swift" ]; then
    cd iosApp && mint run swiftlint lint --config ../.swiftlint.yml ../shared/src/iosMain/swift && cd ..
fi

echo "✅ Swift code formatting complete!"
