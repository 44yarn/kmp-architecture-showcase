//
// AppFonts.swift
// Showcase
//
//

import ShowcaseKit
import SwiftUI

/// A font style that exposes regular and bold variants.
struct AppFontStyle {
    let fontSize: CGFloat
    let lineHeight: CGFloat
    let letterSpacing: CGFloat?

    var regular: AppFontVariant { AppFontVariant(style: self, weight: .regular) }
    var bold: AppFontVariant { AppFontVariant(style: self, weight: .bold) }
}

/// Combines a `Font` with its `lineHeight` and `letterSpacing` so they can be
/// applied together in one place.
struct AppFontVariant {
    let style: AppFontStyle
    let weight: Font.Weight

    var font: Font { .system(size: style.fontSize, weight: weight) }
}

extension View {
    /// Applies an `AppFontVariant` (font + lineSpacing + tracking) in one call.
    func appFont(_ variant: AppFontVariant) -> some View {
        let extraLineSpacing = variant.style.lineHeight - variant.style.fontSize
        return font(variant.font)
            .lineSpacing(extraLineSpacing > 0 ? extraLineSpacing : 0)
            .tracking(variant.style.letterSpacing ?? 0)
    }
}

/// SwiftUI font tokens generated from `AppTypographyValues` in commonMain.
enum AppFonts {
    static let title1 = from(AppTypographyValues.Title1.shared)
    static let title2 = from(AppTypographyValues.Title2.shared)
    static let title3 = from(AppTypographyValues.Title3.shared)
    static let title4 = from(AppTypographyValues.Title4.shared)
    static let headline = from(AppTypographyValues.Headline.shared)
    static let body = from(AppTypographyValues.Body.shared)
    static let callout = from(AppTypographyValues.Callout.shared)
    static let subheadline = from(AppTypographyValues.Subheadline.shared)
    static let footnote = from(AppTypographyValues.Footnote.shared)
    static let caption1 = from(AppTypographyValues.Caption1.shared)
    static let caption2 = from(AppTypographyValues.Caption2.shared)

    private static func from(_ entry: TypographyEntry) -> AppFontStyle {
        AppFontStyle(
            fontSize: CGFloat(entry.fontSize),
            lineHeight: CGFloat(entry.lineHeight),
            letterSpacing: entry.letterSpacing.map { CGFloat($0.floatValue) }
        )
    }
}
