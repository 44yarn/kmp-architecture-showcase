plugins {
    id("showcase.convention.kmp-feature")
    id("showcase.primitive.metro")
}

android {
    namespace = "io.github.yarn44.kmp.showcase.feature.login"
}

// Compose Multiplatform Resources: generate a typed `Res` class for the
// string resources declared under src/commonMain/composeResources/.
compose.resources {
    publicResClass = true
    packageOfResClass = "io.github.yarn44.kmp.showcase.feature.login.resources"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Needed so that the generated `Res` object and the
            // @Composable stringResource(...) helper are available here.
            implementation(compose.components.resources)
        }
        androidMain.dependencies {
            implementation(libs.composeMaterialIconsExtended)
        }
    }
}
