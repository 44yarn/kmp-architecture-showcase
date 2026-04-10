#!/usr/bin/env bash

set -Eeuo pipefail

# Move to the project root so this script works when invoked from script/.
cd "$(dirname "$0")/.."

# Require mint up-front with a clear error message.
if ! command -v mint >/dev/null 2>&1; then
  echo "❌ 'mint' is not installed. Install it first (e.g. 'brew install mint')."
  echo "   See: https://github.com/yonaskolb/Mint"
  exit 1
fi

echo "📱 Formatting Swift code..."

# Install tools declared in iosApp/Mintfile (no-op if already installed).
(cd iosApp && mint bootstrap)

# SwiftFormat — iosApp/
echo "Running SwiftFormat on iosApp..."
(cd iosApp && mint run swiftformat .)

# SwiftFormat — shared/src/iosMain/swift/ (if present)
if [ -d "shared/src/iosMain/swift" ]; then
  echo "Running SwiftFormat on shared/src/iosMain/swift..."
  (cd iosApp && mint run swiftformat ../shared/src/iosMain/swift)
fi

# SwiftLint autofix — iosApp/
echo "Running SwiftLint autocorrect on iosApp..."
(cd iosApp && mint run swiftlint --fix --config ../.swiftlint.yml .)

if [ -d "shared/src/iosMain/swift" ]; then
  echo "Running SwiftLint autocorrect on shared/src/iosMain/swift..."
  (cd iosApp && mint run swiftlint --fix --config ../.swiftlint.yml ../shared/src/iosMain/swift)
fi

# SwiftLint check — iosApp/
echo "Running SwiftLint check..."
(cd iosApp && mint run swiftlint lint --config ../.swiftlint.yml .)

if [ -d "shared/src/iosMain/swift" ]; then
  (cd iosApp && mint run swiftlint lint --config ../.swiftlint.yml ../shared/src/iosMain/swift)
fi

echo "✅ Swift code formatting complete!"
