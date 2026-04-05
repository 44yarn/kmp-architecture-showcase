import Foundation
import ShowcaseKit

// MARK: - KotlinArray

extension Array where Element: AnyObject {
    /// `KotlinArray<T>` を Swift の `[T]` に変換する
    public init(_ kotlin: KotlinArray<Element>) {
        self = (0..<kotlin.size).map { kotlin.get(index: $0)! }
    }
}

// MARK: - KotlinEnum / KotlinCaseIterable

/// Kotlin enum を Swift の `CaseIterable` として扱うプロトコル
public protocol KotlinCaseIterable: CaseIterable, AnyObject {
    associatedtype Value: AnyObject
    static func values() -> KotlinArray<Value>
}

/// Kotlin enum を Swift の `CaseIterable` かつ `Identifiable` として扱うプロトコル
public protocol KotlinEnum: KotlinCaseIterable, Identifiable {
    var name: String { get }
}

extension KotlinCaseIterable {
    public static var allCases: [Value] { Array(values()) }
}
