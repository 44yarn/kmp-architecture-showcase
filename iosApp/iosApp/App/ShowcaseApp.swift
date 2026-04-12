//
// ShowcaseApp.swift
// Showcase
//
//

import ShowcaseKit
import SwiftUI

@main
struct ShowcaseApp: App {
    init() {
        KoinBootstrapKt.bootstrapKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
