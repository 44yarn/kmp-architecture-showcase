import ShowcaseKit
import SwiftUI

struct LoginView: View {
    private let viewModel = KoinBootstrapKt.getLoginViewModel()
    @State private var uiState = LoginUiState(email: "", password: "", isPasswordVisible: false)
    @State private var isLoading = false
    var onNavigate: (AppRoute) -> Void

    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                // Header
                VStack(spacing: 8) {
                    Text("KMP Showcase")
                        .font(.largeTitle)
                        .fontWeight(.bold)
                    Text("Login")
                        .font(.title2)
                        .foregroundColor(.secondary)
                }
                .padding(.top, 40)

                // Email field
                VStack(alignment: .leading, spacing: 4) {
                    Text("Email")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    TextField("user@example.com", text: Binding(
                        get: { uiState.email },
                        set: { viewModel.onEmailChanged(email: $0) }
                    ))
                    .textFieldStyle(.roundedBorder)
                    .keyboardType(.emailAddress)
                    .textInputAutocapitalization(.never)
                    .autocorrectionDisabled()
                }

                // Password field
                VStack(alignment: .leading, spacing: 4) {
                    Text("Password")
                        .font(.caption)
                        .foregroundColor(.secondary)
                    HStack {
                        if uiState.isPasswordVisible {
                            TextField("Password", text: Binding(
                                get: { uiState.password },
                                set: { viewModel.onPasswordChanged(password: $0) }
                            ))
                        } else {
                            SecureField("Password", text: Binding(
                                get: { uiState.password },
                                set: { viewModel.onPasswordChanged(password: $0) }
                            ))
                        }
                        Button(action: { viewModel.onTogglePasswordVisibility() }) {
                            Image(systemName: uiState.isPasswordVisible
                                  ? "eye.slash" : "eye")
                                .foregroundColor(.secondary)
                        }
                    }
                    .textFieldStyle(.roundedBorder)
                }

                // Loading indicator
                if isLoading {
                    ProgressView()
                        .padding()
                }

                // Buttons
                VStack(spacing: 12) {
                    Button(action: { viewModel.onLogin() }) {
                        Text("Login")
                            .font(.headline)
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(
                                RoundedRectangle(cornerRadius: 12)
                                    .fill(isLoading ? Color.gray : Color.blue)
                            )
                    }
                    .disabled(isLoading)

                    Button(action: { viewModel.onGuestLogin() }) {
                        Text("Guest Login")
                            .font(.headline)
                            .foregroundColor(.blue)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(
                                RoundedRectangle(cornerRadius: 12)
                                    .stroke(Color.blue, lineWidth: 2)
                            )
                    }

                    HStack(spacing: 16) {
                        Button("Random Email") { viewModel.onRandomEmail() }
                            .font(.subheadline)
                        Button("Error Demo") { viewModel.onLoginFailureDemo() }
                            .font(.subheadline)
                            .foregroundColor(.red)
                        Button("Clear") { viewModel.onCancel() }
                            .font(.subheadline)
                    }
                    .padding(.top, 8)

                    Button(action: { onNavigate(.info) }) {
                        Text("Info")
                            .font(.subheadline)
                            .foregroundColor(.secondary)
                    }
                    .padding(.top, 4)
                }
            }
            .padding(.horizontal, 24)
        }
        .navigationBarHidden(true)
        .task {
            for await state in viewModel.uiState {
                uiState = state
            }
        }
        .task {
            for await loading in viewModel.indicatorState.isLoading {
                isLoading = loading.boolValue
            }
        }
        .task {
            for await effect in viewModel.effect {
                switch effect {
                case let navigateToHome as LoginEffectNavigateToHome:
                    onNavigate(.home(
                        displayName: navigateToHome.displayName,
                        isGuest: navigateToHome.isGuest
                    ))
                case is LoginEffectLaunchActivity:
                    onNavigate(.info)
                default:
                    break
                }
            }
        }
    }
}
