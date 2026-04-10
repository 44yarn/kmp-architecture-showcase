//
// AppSpacings.swift
// Showcase
//
//

import ShowcaseKit
import SwiftUI

/// SwiftUI spacing tokens generated from `AppSpacingValues` in commonMain.
enum AppSpacings {
    enum Padding {
        static let xxSmall = CGFloat(AppSpacingValues.Padding.shared.XX_SMALL)
        static let xSmall = CGFloat(AppSpacingValues.Padding.shared.X_SMALL)
        static let small = CGFloat(AppSpacingValues.Padding.shared.SMALL)
        static let medium = CGFloat(AppSpacingValues.Padding.shared.MEDIUM)
        static let large = CGFloat(AppSpacingValues.Padding.shared.LARGE)
        static let xLarge = CGFloat(AppSpacingValues.Padding.shared.X_LARGE)
    }

    enum Gap {
        static let minimal = CGFloat(AppSpacingValues.Gap.shared.MINIMAL)
        static let small = CGFloat(AppSpacingValues.Gap.shared.SMALL)
        static let medium = CGFloat(AppSpacingValues.Gap.shared.MEDIUM)
    }

    enum IconSize {
        static let small = CGFloat(AppSpacingValues.IconSize.shared.SMALL)
        static let medium = CGFloat(AppSpacingValues.IconSize.shared.MEDIUM)
    }
}
