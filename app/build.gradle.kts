plugins {
    id("showcase.convention.app")
    id("showcase.primitive.compose")
    id("showcase.primitive.metro")
    id("showcase.primitive.logging")
    id("showcase.primitive.unit-test")
    id("showcase.primitive.serialization")
}

dependencies {
    implementation(project(":core:foundation"))
    implementation(project(":core:ui"))
    implementation(project(":core:data"))
}
