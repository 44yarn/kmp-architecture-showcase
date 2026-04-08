#!/bin/bash

echo "📱 Formatting Swift code..."

# Mintでツールをインストール（まだの場合）
cd iosApp && mint bootstrap && cd ..

# SwiftFormatを実行
echo "Running SwiftFormat on iosApp..."
cd iosApp && mint run swiftformat . && cd ..

# SwiftLintで自動修正
echo "Running SwiftLint autocorrect on iosApp..."
cd iosApp && mint run swiftlint --fix . && cd ..

# SwiftLintでチェック
echo "Running SwiftLint check..."
cd iosApp && mint run swiftlint lint . && cd ..

echo "✅ Swift code formatting complete!"
