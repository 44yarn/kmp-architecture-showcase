package showcase.primitive

plugins {
    id("com.google.devtools.ksp")
}

// Runtime dependencies and KSP compiler configurations are added
// directly in each module's build.gradle.kts, because:
// 1. kspCommonMainMetadata and kspIosXxx must not be used simultaneously
//    (causes Redeclaration errors with kotlin-inject-anvil)
// 2. configureKmpOrAndroid's commonMain block doesn't correctly add
//    dependencies to KMP sourceSets
