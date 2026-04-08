import ShowcaseKit
import SwiftUI

struct HomeView: View {
    private let viewModel: HomeViewModel
    @State private var uiState: HomeUiState
    var onLogout: () -> Void

    @State private var snackbarMessage: String?

    init(
        displayName: String,
        isGuest: Bool,
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
            savedEmail: "",
            isRememberEmail: false
        ))
        self.onLogout = onLogout
    }

    var body: some View {
        Box_content
            .navigationTitle(uiState.screenTitle)
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
                            DispatchQueue.main.asyncAfter(deadline: .now() + 3) {
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

    private var Box_content: some View {
        VStack(spacing: 16) {
            Spacer()

            Text("Hello, \(uiState.displayName)!")
                .font(.body)

            if !uiState.savedEmail.isEmpty {
                Text("Saved email: \(uiState.savedEmail)")
                    .font(.subheadline)
                    .foregroundColor(.secondary)
            }

            HStack {
                Text("Remember Email")
                Spacer()
                Toggle("", isOn: Binding(
                    get: { uiState.isRememberEmail },
                    set: { _ in viewModel.onToggleRememberEmail() }
                ))
                .labelsHidden()
            }
            .padding(.horizontal, 24)

            Spacer()

            Button(action: { viewModel.onLogout() }) {
                Text("Logout")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.bordered)
            .padding(.horizontal, 24)
            .padding(.bottom, 24)
        }
    }
}
