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
        IosAppComponentKt.bootstrapIosAppComponent()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
