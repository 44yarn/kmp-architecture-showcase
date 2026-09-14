//
// LoginView.swift
// Showcase
//
//

import ShowcaseKit
import SwiftUI

struct LoginView: View {
    @Environment(\.colorScheme) private var colorScheme
    @State private var viewModel = IosAppGraphKt.getLoginViewModel()
    @State private var uiState = LoginUiState(email: "", password: "", isPasswordVisible: false)
    @State private var isLoading = false
    // The dialog's AdaptiveString fields are resolved asynchronously via the
    // commonMain `resolve()` extension (SKIE bridges Kotlin suspend to Swift
    // async), then stored here for SwiftUI to render synchronously.
    @State private var dialogResolved: ResolvedDialog?
    var onNavigate: (AppRoute) -> Void

    private var colors: AppColors { .resolve(colorScheme) }
    private var isLoginDisabled: Bool { isLoading || uiState.email.isEmpty || uiState.password.isEmpty }
    private var isCancelDisabled: Bool { !isLoading }

    var body: some View {
        ZStack {
            boxContent
            if isLoading {
                ProgressView()
            }
        }
        .background(colors.background.ignoresSafeArea())
        .navigationBarHidden(true)
        .alert(
            dialogResolved?.title ?? "",
            isPresented: Binding(
                get: { dialogResolved != nil },
                set: { if !$0 { viewModel.dialogPresenter.onDismiss(); dialogResolved = nil } }
            )
        ) {
            Button(dialogResolved?.positiveButton ?? "OK") {
                viewModel.dialogPresenter.onPositive()
                dialogResolved = nil
            }
            if let negativeButton = dialogResolved?.negativeButton {
                Button(negativeButton, role: .cancel) {
                    viewModel.dialogPresenter.onNegative()
                    dialogResolved = nil
                }
            }
        } message: {
            Text(dialogResolved?.message ?? "")
                .appFont(AppFonts.body.regular)
                .foregroundColor(colors.onSurface)
        }
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
                case is LoginEffectNavigateToInfo:
                    onNavigate(.info)
                default:
                    break
                }
            }
        }
        .task {
            for await state in viewModel.dialogPresenter.uiState {
                if let state {
                    dialogResolved = await ResolvedDialog.from(state)
                } else {
                    dialogResolved = nil
                }
            }
        }
    }

    /// Plain-String projection of a [DialogUiState] for SwiftUI consumption.
    /// The conversion awaits `AdaptiveString.resolve()` for each non-nil field.
    private struct ResolvedDialog {
        let title: String
        let message: String
        let positiveButton: String
        let negativeButton: String?

        static func from(_ state: DialogUiState) async -> ResolvedDialog {
            // SKIE bridges Kotlin suspend functions to Swift async throws,
            // so `try?` is used to fall back to the default text on failure.
            let title = await (try? state.title?.resolve()) ?? ""
            let message = await (try? state.message?.resolve()) ?? ""
            let positive = await (try? state.positiveButton?.resolve()) ?? "OK"
            let negative: String? = if let neg = state.negativeButton {
                try? await neg.resolve()
            } else {
                nil
            }
            return ResolvedDialog(
                title: title,
                message: message,
                positiveButton: positive,
                negativeButton: negative
            )
        }
    }

    private var boxContent: some View {
        VStack(spacing: AppSpacings.Padding.medium) {
            Spacer()

            Text("KMP Showcase")
                .appFont(AppFonts.title2.bold)
                .foregroundColor(colors.onBackground)

            Spacer().frame(height: AppSpacings.Padding.xSmall)

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
                        .foregroundColor(colors.onSurfaceVariant)
                }
            }
            .padding(AppSpacings.Padding.small)
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(colors.outline, lineWidth: 1)
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
                        .foregroundColor(colors.onSurfaceVariant)
                }
            }
            .padding(AppSpacings.Padding.small)
            .overlay(
                RoundedRectangle(cornerRadius: 4)
                    .stroke(colors.outline, lineWidth: 1)
            )

            // Login button
            Button(action: { viewModel.onLogin() }) {
                Text("Login")
                    .appFont(AppFonts.headline.bold)
                    .foregroundColor(colors.onPrimary)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(
                        RoundedRectangle(cornerRadius: AppCornerRadii.small)
                            .fill(isLoginDisabled ? colors.secondary : colors.primary)
                    )
            }
            .disabled(isLoginDisabled)

            // Login (Fail) / Cancel row
            HStack(spacing: AppSpacings.Padding.xSmall) {
                Button(action: { viewModel.onLoginFailureDemo() }) {
                    Text("Login (Fail)")
                        .appFont(AppFonts.body.regular)
                        .foregroundColor(isLoading ? colors.secondary : colors.primary)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(
                            RoundedRectangle(cornerRadius: AppCornerRadii.small)
                                .stroke(isLoading ? colors.secondary : colors.primary, lineWidth: 1)
                        )
                }
                .disabled(isLoading)

                Button(action: { viewModel.onCancel() }) {
                    Text("Cancel")
                        .appFont(AppFonts.body.regular)
                        .foregroundColor(isCancelDisabled ? colors.secondary : colors.primary)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(
                            RoundedRectangle(cornerRadius: AppCornerRadii.small)
                                .stroke(isCancelDisabled ? colors.secondary : colors.primary, lineWidth: 1)
                        )
                }
                .disabled(isCancelDisabled)
            }

            // Information button
            Button(action: { onNavigate(.info) }) {
                Text("Information")
                    .appFont(AppFonts.body.regular)
                    .foregroundColor(colors.primary)
            }
            .disabled(isLoading)

            Spacer()
        }
        .padding(.horizontal, AppSpacings.Padding.xLarge)
    }
}
