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
        IosAppGraphKt.bootstrapIosAppGraph()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
