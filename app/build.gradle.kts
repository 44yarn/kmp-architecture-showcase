plugins {
    id("showcase.convention.app")
    id("showcase.primitive.compose")
    id("showcase.primitive.kotlin-inject")
    id("showcase.primitive.logging")
    id("showcase.primitive.unit-test")
    id("showcase.primitive.serialization")
}

dependencies {
    implementation(project(":core:foundation"))
    implementation(project(":core:ui"))
    api(project(":core:data"))

    implementation(libs.kotlinInjectRuntime)
    implementation(libs.kotlinInjectAnvilRuntime)
    implementation(libs.kotlinInjectAnvilRuntimeOptional)

    add("ksp", libs.kotlinInjectCompiler)
    add("ksp", libs.kotlinInjectAnvilCompiler)
}
