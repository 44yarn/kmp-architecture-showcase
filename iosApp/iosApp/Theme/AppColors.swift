//
// AppColors.swift
// Showcase
//

import ShowcaseKit
import SwiftUI

/// commonMain の AppColorValues から生成される SwiftUI カラートークン
struct AppColors {
    // Primary
    let primary: Color
    let onPrimary: Color
    let primaryContainer: Color
    let onPrimaryContainer: Color
    let inversePrimary: Color
    let primaryFixed: Color
    let onPrimaryFixed: Color
    let primaryFixedDim: Color
    let onPrimaryFixedVariant: Color

    // Secondary
    let secondary: Color
    let onSecondary: Color
    let secondaryContainer: Color
    let onSecondaryContainer: Color
    let secondaryFixed: Color
    let onSecondaryFixed: Color
    let secondaryFixedDim: Color
    let onSecondaryFixedVariant: Color

    // Tertiary
    let tertiary: Color
    let onTertiary: Color
    let tertiaryContainer: Color
    let onTertiaryContainer: Color
    let tertiaryFixed: Color
    let onTertiaryFixed: Color
    let tertiaryFixedDim: Color
    let onTertiaryFixedVariant: Color

    // Error
    let error: Color
    let onError: Color
    let errorContainer: Color
    let onErrorContainer: Color

    // Neutrals / Surfaces
    let background: Color
    let onBackground: Color
    let surface: Color
    let onSurface: Color
    let surfaceVariant: Color
    let onSurfaceVariant: Color

    // Extended surfaces
    let surfaceDim: Color
    let surfaceBright: Color
    let surfaceContainerLowest: Color
    let surfaceContainerLow: Color
    let surfaceContainer: Color
    let surfaceContainerHigh: Color
    let surfaceContainerHighest: Color

    // Inverse & outline
    let inverseSurface: Color
    let inverseOnSurface: Color
    let outline: Color
    let outlineVariant: Color

    // Misc
    let scrim: Color
    let surfaceTint: Color

    // App-specific
    let overlay: Color

    static func resolve(_ colorScheme: ColorScheme) -> AppColors {
        colorScheme == .dark ? .dark : .light
    }

    static let light: AppColors = {
        let v = AppColorValues.Light.shared
        return AppColors(
            primary: Color(argb: v.PRIMARY),
            onPrimary: Color(argb: v.ON_PRIMARY),
            primaryContainer: Color(argb: v.PRIMARY_CONTAINER),
            onPrimaryContainer: Color(argb: v.ON_PRIMARY_CONTAINER),
            inversePrimary: Color(argb: v.INVERSE_PRIMARY),
            primaryFixed: Color(argb: v.PRIMARY_FIXED),
            onPrimaryFixed: Color(argb: v.ON_PRIMARY_FIXED),
            primaryFixedDim: Color(argb: v.PRIMARY_FIXED_DIM),
            onPrimaryFixedVariant: Color(argb: v.ON_PRIMARY_FIXED_VARIANT),
            secondary: Color(argb: v.SECONDARY),
            onSecondary: Color(argb: v.ON_SECONDARY),
            secondaryContainer: Color(argb: v.SECONDARY_CONTAINER),
            onSecondaryContainer: Color(argb: v.ON_SECONDARY_CONTAINER),
            secondaryFixed: Color(argb: v.SECONDARY_FIXED),
            onSecondaryFixed: Color(argb: v.ON_SECONDARY_FIXED),
            secondaryFixedDim: Color(argb: v.SECONDARY_FIXED_DIM),
            onSecondaryFixedVariant: Color(argb: v.ON_SECONDARY_FIXED_VARIANT),
            tertiary: Color(argb: v.TERTIARY),
            onTertiary: Color(argb: v.ON_TERTIARY),
            tertiaryContainer: Color(argb: v.TERTIARY_CONTAINER),
            onTertiaryContainer: Color(argb: v.ON_TERTIARY_CONTAINER),
            tertiaryFixed: Color(argb: v.TERTIARY_FIXED),
            onTertiaryFixed: Color(argb: v.ON_TERTIARY_FIXED),
            tertiaryFixedDim: Color(argb: v.TERTIARY_FIXED_DIM),
            onTertiaryFixedVariant: Color(argb: v.ON_TERTIARY_FIXED_VARIANT),
            error: Color(argb: v.ERROR),
            onError: Color(argb: v.ON_ERROR),
            errorContainer: Color(argb: v.ERROR_CONTAINER),
            onErrorContainer: Color(argb: v.ON_ERROR_CONTAINER),
            background: Color(argb: v.BACKGROUND),
            onBackground: Color(argb: v.ON_BACKGROUND),
            surface: Color(argb: v.SURFACE),
            onSurface: Color(argb: v.ON_SURFACE),
            surfaceVariant: Color(argb: v.SURFACE_VARIANT),
            onSurfaceVariant: Color(argb: v.ON_SURFACE_VARIANT),
            surfaceDim: Color(argb: v.SURFACE_DIM),
            surfaceBright: Color(argb: v.SURFACE_BRIGHT),
            surfaceContainerLowest: Color(argb: v.SURFACE_CONTAINER_LOWEST),
            surfaceContainerLow: Color(argb: v.SURFACE_CONTAINER_LOW),
            surfaceContainer: Color(argb: v.SURFACE_CONTAINER),
            surfaceContainerHigh: Color(argb: v.SURFACE_CONTAINER_HIGH),
            surfaceContainerHighest: Color(argb: v.SURFACE_CONTAINER_HIGHEST),
            inverseSurface: Color(argb: v.INVERSE_SURFACE),
            inverseOnSurface: Color(argb: v.INVERSE_ON_SURFACE),
            outline: Color(argb: v.OUTLINE),
            outlineVariant: Color(argb: v.OUTLINE_VARIANT),
            scrim: Color(argb: v.SCRIM),
            surfaceTint: Color(argb: v.SURFACE_TINT),
            overlay: Color(argb: v.OVERLAY)
        )
    }()

    static let dark: AppColors = {
        let v = AppColorValues.Dark.shared
        return AppColors(
            primary: Color(argb: v.PRIMARY),
            onPrimary: Color(argb: v.ON_PRIMARY),
            primaryContainer: Color(argb: v.PRIMARY_CONTAINER),
            onPrimaryContainer: Color(argb: v.ON_PRIMARY_CONTAINER),
            inversePrimary: Color(argb: v.INVERSE_PRIMARY),
            primaryFixed: Color(argb: v.PRIMARY_FIXED),
            onPrimaryFixed: Color(argb: v.ON_PRIMARY_FIXED),
            primaryFixedDim: Color(argb: v.PRIMARY_FIXED_DIM),
            onPrimaryFixedVariant: Color(argb: v.ON_PRIMARY_FIXED_VARIANT),
            secondary: Color(argb: v.SECONDARY),
            onSecondary: Color(argb: v.ON_SECONDARY),
            secondaryContainer: Color(argb: v.SECONDARY_CONTAINER),
            onSecondaryContainer: Color(argb: v.ON_SECONDARY_CONTAINER),
            secondaryFixed: Color(argb: v.SECONDARY_FIXED),
            onSecondaryFixed: Color(argb: v.ON_SECONDARY_FIXED),
            secondaryFixedDim: Color(argb: v.SECONDARY_FIXED_DIM),
            onSecondaryFixedVariant: Color(argb: v.ON_SECONDARY_FIXED_VARIANT),
            tertiary: Color(argb: v.TERTIARY),
            onTertiary: Color(argb: v.ON_TERTIARY),
            tertiaryContainer: Color(argb: v.TERTIARY_CONTAINER),
            onTertiaryContainer: Color(argb: v.ON_TERTIARY_CONTAINER),
            tertiaryFixed: Color(argb: v.TERTIARY_FIXED),
            onTertiaryFixed: Color(argb: v.ON_TERTIARY_FIXED),
            tertiaryFixedDim: Color(argb: v.TERTIARY_FIXED_DIM),
            onTertiaryFixedVariant: Color(argb: v.ON_TERTIARY_FIXED_VARIANT),
            error: Color(argb: v.ERROR),
            onError: Color(argb: v.ON_ERROR),
            errorContainer: Color(argb: v.ERROR_CONTAINER),
            onErrorContainer: Color(argb: v.ON_ERROR_CONTAINER),
            background: Color(argb: v.BACKGROUND),
            onBackground: Color(argb: v.ON_BACKGROUND),
            surface: Color(argb: v.SURFACE),
            onSurface: Color(argb: v.ON_SURFACE),
            surfaceVariant: Color(argb: v.SURFACE_VARIANT),
            onSurfaceVariant: Color(argb: v.ON_SURFACE_VARIANT),
            surfaceDim: Color(argb: v.SURFACE_DIM),
            surfaceBright: Color(argb: v.SURFACE_BRIGHT),
            surfaceContainerLowest: Color(argb: v.SURFACE_CONTAINER_LOWEST),
            surfaceContainerLow: Color(argb: v.SURFACE_CONTAINER_LOW),
            surfaceContainer: Color(argb: v.SURFACE_CONTAINER),
            surfaceContainerHigh: Color(argb: v.SURFACE_CONTAINER_HIGH),
            surfaceContainerHighest: Color(argb: v.SURFACE_CONTAINER_HIGHEST),
            inverseSurface: Color(argb: v.INVERSE_SURFACE),
            inverseOnSurface: Color(argb: v.INVERSE_ON_SURFACE),
            outline: Color(argb: v.OUTLINE),
            outlineVariant: Color(argb: v.OUTLINE_VARIANT),
            scrim: Color(argb: v.SCRIM),
            surfaceTint: Color(argb: v.SURFACE_TINT),
            overlay: Color(argb: v.OVERLAY)
        )
    }()
}
