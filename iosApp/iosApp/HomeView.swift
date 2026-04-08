import ShowcaseKit
import SwiftUI

struct HomeView: View {
    private let viewModel: HomeViewModel
    @State private var uiState: HomeUiState
    var onNavigate: (AppRoute) -> Void
    var onLogout: () -> Void

    @State private var snackbarMessage: String?

    init(
        displayName: String,
        isGuest: Bool,
        onNavigate: @escaping (AppRoute) -> Void,
        onLogout: @escaping () -> Void
    ) {
        let vm = KoinBootstrapKt.getHomeViewModel(
            displayName: displayName,
            isGuest: isGuest
        )
        self.viewModel = vm
        self._uiState = State(initialValue: HomeUiState(
            displayName: displayName,
            isGuest: isGuest,
            savedEmail: nil,
            isRememberEmail: false
        ))
        self.onNavigate = onNavigate
        self.onLogout = onLogout
    }

    var body: some View {
        VStack(spacing: 24) {
            // Welcome section
            VStack(spacing: 8) {
                Text("Welcome")
                    .font(.title2)
                    .foregroundColor(.secondary)
                Text(uiState.displayName)
                    .font(.largeTitle)
                    .fontWeight(.bold)

                if uiState.isGuest {
                    Text("Guest Mode")
                        .font(.caption)
                        .foregroundColor(.orange)
                        .padding(.horizontal, 12)
                        .padding(.vertical, 4)
                        .background(
                            Capsule()
                                .fill(Color.orange.opacity(0.15))
                        )
                }
            }
            .padding(.top, 24)

            Divider()

            // Preferences section
            VStack(spacing: 12) {
                if let savedEmail = uiState.savedEmail {
                    HStack {
                        Text("Saved Email")
                            .foregroundColor(.secondary)
                        Spacer()
                        Text(savedEmail)
                            .foregroundColor(.primary)
                    }
                }

                Toggle(
                    "Remember Email",
                    isOn: Binding(
                        get: { uiState.isRememberEmail },
                        set: { _ in viewModel.onToggleRememberEmail() }
                    )
                )
            }
            .padding(.horizontal)

            Spacer()

            // Buttons
            VStack(spacing: 12) {
                Button(action: { onNavigate(.info) }) {
                    Text("App Info")
                        .font(.headline)
                        .foregroundColor(.blue)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(Color.blue, lineWidth: 2)
                        )
                }

                Button(action: { viewModel.onLogout() }) {
                    Text("Logout")
                        .font(.headline)
                        .foregroundColor(.red)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(Color.red, lineWidth: 2)
                        )
                }
            }
            .padding(.horizontal, 24)
            .padding(.bottom, 24)
        }
        .navigationTitle("Home")
        .navigationBarBackButtonHidden(true)
        .overlay(alignment: .bottom) {
            if let message = snackbarMessage {
                Text(message)
                    .font(.subheadline)
                    .foregroundColor(.white)
                    .padding(.horizontal, 16)
                    .padding(.vertical, 10)
                    .background(
                        RoundedRectangle(cornerRadius: 8)
                            .fill(Color.black.opacity(0.8))
                    )
                    .padding(.bottom, 100)
                    .transition(.move(edge: .bottom).combined(with: .opacity))
                    .onAppear {
                        DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                            withAnimation { snackbarMessage = nil }
                        }
                    }
            }
        }
        .task {
            for await state in viewModel.uiState {
                uiState = state
            }
        }
        .task {
            for await effect in viewModel.effect {
                if effect is HomeEffectNavigateToLogin {
                    onLogout()
                }
            }
        }
        .onAppear {
            if let msg = viewModel.getSnackbarMessage() {
                withAnimation { snackbarMessage = msg }
            }
        }
    }
}
