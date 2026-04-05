import ShowcaseKit
import SwiftUI

struct InfoView: View {
    var body: some View {
        VStack(spacing: 24) {
            Spacer()

            Image(systemName: "info.circle.fill")
                .font(.system(size: 60))
                .foregroundColor(.blue)

            Text(InfoContent.shared.TITLE)
                .font(.title)
                .fontWeight(.bold)
                .multilineTextAlignment(.center)

            Text(InfoContent.shared.DESCRIPTION)
                .font(.body)
                .foregroundColor(.secondary)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 32)

            Text("Version \(InfoContent.shared.VERSION)")
                .font(.caption)
                .foregroundColor(.secondary)

            Spacer()
        }
        .navigationTitle("Info")
    }
}
