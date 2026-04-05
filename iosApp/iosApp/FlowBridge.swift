import ShowcaseKit

/// KMP-NativeCoroutines が生成するコールバックスタイルの Flow を Swift AsyncStream に変換するヘルパー
enum FlowBridge {

    /// NativeCoroutines の NativeFlow (callback-style) を AsyncStream に変換
    static func asyncStream<T: AnyObject>(
        for nativeFlow: @escaping (
            @escaping (T, @escaping () -> KotlinUnit, KotlinUnit) -> KotlinUnit,
            @escaping ((any Error)?, KotlinUnit) -> KotlinUnit,
            @escaping (any Error, KotlinUnit) -> KotlinUnit
        ) -> () -> KotlinUnit
    ) -> AsyncStream<T> {
        AsyncStream { continuation in
            let cancel = nativeFlow(
                { item, _, _ in
                    continuation.yield(item)
                    return KotlinUnit()
                },
                { _, _ in
                    continuation.finish()
                    return KotlinUnit()
                },
                { _, _ in
                    continuation.finish()
                    return KotlinUnit()
                }
            )
            continuation.onTermination = { _ in
                _ = cancel()
            }
        }
    }
}
