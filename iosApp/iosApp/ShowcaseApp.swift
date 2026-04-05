import ShowcaseKit
import SwiftUI

@main
struct ShowcaseApp: App {
    init() {
        KoinBootstrapKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
