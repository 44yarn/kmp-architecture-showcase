// swift-tools-version: 6.0
//
// SPM package that exposes the Kotlin/Native produced ShowcaseKit.xcframework
// to the iOS app as a consumable Swift Package via `binaryTarget`.
//
// The XCFramework itself is produced by the Gradle task
// `:shared:assembleShowcaseKitDebugXCFramework` and lives at
// `build/XCFrameworks/debug/ShowcaseKit.xcframework`.
//
// Note on debug-only path:
//   This package currently points at the Debug variant only. Release
//   distribution would require either switching to a Release XCFramework
//   path, or using two separate package products. For a development
//   showcase, Debug is sufficient.

import PackageDescription

let package = Package(
    name: "ShowcaseKit",
    platforms: [
        .iOS(.v18),
    ],
    products: [
        .library(
            name: "ShowcaseKit",
            targets: ["ShowcaseKit"],
        ),
    ],
    targets: [
        .binaryTarget(
            name: "ShowcaseKit",
            path: "build/XCFrameworks/debug/ShowcaseKit.xcframework",
        ),
    ],
)
