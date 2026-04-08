//
// Color+App.swift
// Showcase
//
//

import SwiftUI

extension Color {
    /// Kotlin Long（ARGB hex）から SwiftUI Color を生成する
    init(argb value: Int64) {
        let a = Double((value >> 24) & 0xFF) / 255.0
        let r = Double((value >> 16) & 0xFF) / 255.0
        let g = Double((value >> 8) & 0xFF) / 255.0
        let b = Double(value & 0xFF) / 255.0
        self.init(.sRGB, red: r, green: g, blue: b, opacity: a)
    }
}
