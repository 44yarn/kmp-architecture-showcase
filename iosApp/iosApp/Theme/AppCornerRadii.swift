//
// AppCornerRadii.swift
// Showcase
//

import ShowcaseKit
import SwiftUI

/// commonMain の AppCornerRadiusValues から生成される SwiftUI コーナー半径トークン
enum AppCornerRadii {
    static let small = CGFloat(AppCornerRadiusValues.shared.SMALL)
    static let medium = CGFloat(AppCornerRadiusValues.shared.MEDIUM)
    static let large = CGFloat(AppCornerRadiusValues.shared.LARGE)
    static let extraLarge = CGFloat(AppCornerRadiusValues.shared.EXTRA_LARGE)
    static let full = CGFloat(AppCornerRadiusValues.shared.FULL)
}
