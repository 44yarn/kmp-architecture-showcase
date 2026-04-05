import SwiftUI

enum AppRoute: Hashable {
    case home(displayName: String, isGuest: Bool)
    case info
}

struct ContentView: View {
    @State private var path = NavigationPath()

    var body: some View {
        NavigationStack(path: $path) {
            LoginView { route in
                path.append(route)
            }
            .navigationDestination(for: AppRoute.self) { route in
                switch route {
                case let .home(displayName, isGuest):
                    HomeView(
                        displayName: displayName,
                        isGuest: isGuest,
                        onNavigate: { route in path.append(route) },
                        onLogout: { path = NavigationPath() }
                    )
                case .info:
                    InfoView()
                }
            }
        }
    }
}
