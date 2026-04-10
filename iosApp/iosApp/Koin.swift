//
// Koin.swift
// Showcase
//
//

import Foundation
import ShowcaseKit

// MARK: - KotlinArray

public extension Array where Element: AnyObject {
    /// Converts a `KotlinArray<T>` into a Swift `[T]`.
    init(_ kotlin: KotlinArray<Element>) {
        self = (0 ..< kotlin.size).map { kotlin.get(index: $0)! }
    }
}

// MARK: - KotlinEnum / KotlinCaseIterable

/// A protocol that lets a Kotlin enum be used as a Swift `CaseIterable`.
public protocol KotlinCaseIterable: CaseIterable, AnyObject {
    associatedtype Value: AnyObject
    static func values() -> KotlinArray<Value>
}

/// A protocol that lets a Kotlin enum be used as both `CaseIterable`
/// and `Identifiable` from Swift.
public protocol KotlinEnum: KotlinCaseIterable, Identifiable {
    var name: String { get }
}

public extension KotlinCaseIterable {
    static var allCases: [Value] { Array(values()) }
}
