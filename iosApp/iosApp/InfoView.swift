import ShowcaseKit
import SwiftUI

struct InfoView: View {
    @Environment(\.colorScheme) private var colorScheme

    private var colors: AppColors { .resolve(colorScheme) }

    var body: some View {
        VStack(spacing: AppSpacings.Padding.xLarge) {
            Spacer()

            Image(systemName: "info.circle.fill")
                .font(.system(size: 60))
                .foregroundColor(colors.primary)

            Text(InfoContent.shared.TITLE)
                .appFont(AppFonts.title3.bold)
                .foregroundColor(colors.onBackground)
                .multilineTextAlignment(.center)

            Text(InfoContent.shared.DESCRIPTION)
                .appFont(AppFonts.body.regular)
                .foregroundColor(colors.onSurfaceVariant)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)

            Text("Version \(InfoContent.shared.VERSION)")
                .appFont(AppFonts.caption1.regular)
                .foregroundColor(colors.onSurfaceVariant)

            Spacer()
        }
        .background(colors.background.ignoresSafeArea())
        .navigationTitle("Info")
    }
}
