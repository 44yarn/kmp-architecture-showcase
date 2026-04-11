//
// HomeView.swift
// Showcase
//
//

import ShowcaseKit
import SwiftUI

struct HomeView: View {
    @Environment(\.colorScheme) private var colorScheme
    private let viewModel: HomeViewModel
    @State private var uiState: HomeUiState
    var onLogout: () -> Void

    @State private var snackbarMessage: String?

    private var colors: AppColors { .resolve(colorScheme) }

    init(
        displayName: String,
        isGuest: Bool,
        onLogout: @escaping () -> Void
    ) {
        viewModel = KoinBootstrapKt.getHomeViewModel(
            displayName: displayName,
            isGuest: isGuest
        )
        _uiState = State(initialValue: HomeUiState(
            displayName: displayName,
            isGuest: isGuest,
            savedEmail: nil,
            isRememberEmail: true
        ))
        self.onLogout = onLogout
    }

    var body: some View {
        boxContent
            .background(colors.background.ignoresSafeArea())
            .navigationTitle(uiState.screenTitle)
            .navigationBarBackButtonHidden(true)
            .overlay(alignment: .bottom) {
                if let message = snackbarMessage {
                    Text(message)
                        .appFont(AppFonts.subheadline.regular)
                        .foregroundColor(colors.inverseOnSurface)
                        .padding(.horizontal, AppSpacings.Padding.medium)
                        .padding(.vertical, AppSpacings.Padding.small)
                        .background(
                            RoundedRectangle(cornerRadius: AppCornerRadii.small)
                                .fill(colors.inverseSurface)
                        )
                        .padding(.bottom, 100)
                        .transition(.move(edge: .bottom).combined(with: .opacity))
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
            .task {
                for await state in viewModel.snackbarPresenter.uiState {
                    withAnimation { snackbarMessage = state?.message }
                }
            }
    }

    private var boxContent: some View {
        VStack(spacing: AppSpacings.Padding.medium) {
            Spacer()

            Text("Hello, \(uiState.displayName)!")
                .appFont(AppFonts.body.regular)
                .foregroundColor(colors.onBackground)

            if let savedEmail = uiState.savedEmail {
                Text("Saved email: \(savedEmail)")
                    .appFont(AppFonts.subheadline.regular)
                    .foregroundColor(colors.onSurfaceVariant)
            }

            HStack {
                Text("Remember Email")
                    .appFont(AppFonts.body.regular)
                    .foregroundColor(colors.onBackground)
                Spacer()
                Toggle("", isOn: Binding(
                    get: { uiState.isRememberEmail },
                    set: { _ in viewModel.onToggleRememberEmail() }
                ))
                .labelsHidden()
            }
            .padding(.horizontal, AppSpacings.Padding.xLarge)

            Spacer()

            Button(action: { viewModel.onLogout() }) {
                Text("Logout")
                    .appFont(AppFonts.headline.bold)
                    .foregroundColor(colors.primary)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(
                        RoundedRectangle(cornerRadius: AppCornerRadii.small)
                            .stroke(colors.primary, lineWidth: 1)
                    )
            }
            .padding(.horizontal, AppSpacings.Padding.xLarge)
            .padding(.bottom, AppSpacings.Padding.xLarge)
        }
    }
}
