plugins {
    id("showcase.convention.app")
    id("showcase.primitive.compose")
    id("dev.zacsweers.metro")
    id("showcase.primitive.logging")
    id("showcase.primitive.unit-test")
    id("showcase.primitive.serialization")
}

dependencies {
    implementation(project(":core:foundation"))
    implementation(project(":core:ui"))
    api(project(":core:data"))
}
