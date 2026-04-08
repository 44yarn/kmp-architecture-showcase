import ShowcaseKit
import SwiftUI

struct LoginView: View {
    private let viewModel = KoinBootstrapKt.getLoginViewModel()
    @State private var uiState = LoginUiState(email: "", password: "", isPasswordVisible: false)
    @State private var isLoading = false
    var onNavigate: (AppRoute) -> Void

    var body: some View {
        ZStack {
            Box_content
            if isLoading {
                ProgressView()
            }
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

    private var Box_content: some View {
        VStack(spacing: 16) {
            Text("KMP Showcase")
                .font(.title2)
                .fontWeight(.semibold)
                .padding(.top, 40)

            Spacer().frame(height: 8)

            // Email field
            HStack {
                TextField("Email", text: Binding(
                    get: { uiState.email },
                    set: { viewModel.onEmailChanged(email: $0) }
                ))
                .keyboardType(.emailAddress)
                .textInputAutocapitalization(.never)
                .autocorrectionDisabled()

                Button(action: { viewModel.onRandomEmail() }) {
                    Image(systemName: "arrow.clockwise")
                        .foregroundColor(.secondary)
                }
            }
            .padding(12)
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(Color(.systemGray3), lineWidth: 1)
            )

            // Password field
            HStack {
                Group {
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
                }
                Button(action: { viewModel.onTogglePasswordVisibility() }) {
                    Image(systemName: uiState.isPasswordVisible ? "eye.slash" : "eye")
                        .foregroundColor(.secondary)
                }
            }
            .padding(12)
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(Color(.systemGray3), lineWidth: 1)
            )

            // Login button
            Button(action: { viewModel.onLogin() }) {
                Text("Login")
                    .font(.headline)
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .disabled(isLoading || uiState.email.isEmpty || uiState.password.isEmpty)

            // Login (Fail) / Cancel row
            HStack(spacing: 8) {
                Button(action: { viewModel.onLoginFailureDemo() }) {
                    Text("Login (Fail)")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.bordered)
                .disabled(isLoading)

                Button(action: { viewModel.onCancel() }) {
                    Text("Cancel")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.bordered)
                .disabled(!isLoading)
            }

            // Information button
            Button(action: { onNavigate(.info) }) {
                Text("Information")
            }
            .disabled(isLoading)

            Spacer()
        }
        .padding(.horizontal, 24)
    }
}
